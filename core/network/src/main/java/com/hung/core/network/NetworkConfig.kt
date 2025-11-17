package com.hung.core.network

/**
 * Configuration class for network settings.
 */
data class NetworkConfig(
    val baseUrl: String,
    val connectTimeoutSeconds: Long,
    val readTimeoutSeconds: Long,
    val writeTimeoutSeconds: Long,
    val enableLogging: Boolean,
    val retryCount: Int,
    val retryDelayMs: Long
)

class NetworkConfigBuilder {
    companion object {
        private const val CONNECT_TIMEOUT_IN_SECOND = 30L
        private const val READ_TIMEOUT_IN_SECOND = 30L
        private const val WRITE_TIMEOUT_IN_SECOND = 30L
        private const val RETRY_COUNT = 3
        private const val RETRY_DELAY_IN_MILLIS = 1000L
    }
    private var baseUrl: String = ""
    private var connectTimeoutSeconds: Long = CONNECT_TIMEOUT_IN_SECOND
    private var readTimeoutSeconds: Long = READ_TIMEOUT_IN_SECOND
    private var writeTimeoutSeconds: Long = WRITE_TIMEOUT_IN_SECOND
    private var enableLogging: Boolean = false
    private var retryCount: Int = RETRY_COUNT
    private var retryDelayMs: Long = RETRY_DELAY_IN_MILLIS
    
    fun baseUrl(url: String) = apply { baseUrl = url }
    fun connectTimeout(seconds: Long) = apply { connectTimeoutSeconds = seconds }
    fun readTimeout(seconds: Long) = apply { readTimeoutSeconds = seconds }
    fun writeTimeout(seconds: Long) = apply { writeTimeoutSeconds = seconds }
    fun enableLogging(enable: Boolean) = apply { enableLogging = enable }
    fun retryCount(count: Int) = apply { retryCount = count }
    fun retryDelay(delayMs: Long) = apply { retryDelayMs = delayMs }
    
    fun build(): NetworkConfig = NetworkConfig(
        baseUrl = baseUrl,
        connectTimeoutSeconds = connectTimeoutSeconds,
        readTimeoutSeconds = readTimeoutSeconds,
        writeTimeoutSeconds = writeTimeoutSeconds,
        enableLogging = enableLogging,
        retryCount = retryCount,
        retryDelayMs = retryDelayMs
    )
}

fun networkConfig(block: NetworkConfigBuilder.() -> Unit): NetworkConfig {
    return NetworkConfigBuilder().apply(block).build()
}
