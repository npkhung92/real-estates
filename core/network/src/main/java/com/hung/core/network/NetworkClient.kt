package com.hung.core.network

import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interface for network client operations.
 */
interface NetworkClient {
    fun <T> createService(serviceClass: Class<T>): T

    suspend fun <T> executeWithRetry(
        retryCount: Int? = null,
        operation: suspend () -> T,
    ): NetworkResult<T>
}

/**
 * Default implementation of NetworkClient using Retrofit.
 */
@Singleton
class DefaultNetworkClient @Inject constructor(
    private val logger: NetworkLogger,
    private val config: NetworkConfig,
) : NetworkClient {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .client(createOkHttpClient())
            .addConverterFactory(createJson().asConverterFactory("application/json".toMediaType()))
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        val builder = with(config) {
            OkHttpClient.Builder()
                .connectTimeout(connectTimeoutSeconds, TimeUnit.SECONDS)
                .readTimeout(readTimeoutSeconds, TimeUnit.SECONDS)
                .writeTimeout(writeTimeoutSeconds, TimeUnit.SECONDS)
        }
        if (config.enableLogging) {
            val loggingInterceptor = HttpLoggingInterceptor { message ->
                logger.debug("NetworkClient: $message")
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    private fun createJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        coerceInputValues = true
    }

    override fun <T> createService(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    /**
     * Execute the request with exponential backoff retry attempts
     */
    override suspend fun <T> executeWithRetry(
        retryCount: Int?,
        operation: suspend () -> T,
    ): NetworkResult<T> {
        val maxRetries = retryCount ?: config.retryCount
        var lastException: Throwable? = null

        repeat(maxRetries + 1) { attempt ->
            try {
                logger.debug("Executing network operation, attempt ${attempt + 1}/${maxRetries + 1}")
                val result = operation()
                logger.debug("Network operation completed successfully")
                return NetworkResult.Success(result)
            } catch (exception: Throwable) {
                lastException = exception
                val networkError = exception.toNetworkError()

                logger.error("Network operation failed on attempt ${attempt + 1}: ${networkError.message}", exception)
                if (attempt < maxRetries) {
                    val delayMs = config.retryDelayMs * (attempt + 1)
                    logger.debug("Retrying in ${delayMs}ms...")
                    delay(delayMs)
                }
            }
        }

        // All retries exhausted
        val finalError = lastException?.toNetworkError() ?: NetworkError.UnknownError("All retry attempts failed")
        logger.error("All retry attempts exhausted, returning error: ${finalError.message}")
        return NetworkResult.Error(finalError)
    }
}
