package com.hung.core.network

import android.util.Log

/**
 * Interface for network logging functionality.
 */
interface NetworkLogger {
    fun debug(message: String, throwable: Throwable? = null)

    fun info(message: String, throwable: Throwable? = null)

    fun warning(message: String, throwable: Throwable? = null)

    fun error(message: String, throwable: Throwable? = null)

    fun logRequest(url: String, method: String, headers: Map<String, String>? = null)

    fun logResponse(
        url: String,
        method: String,
        httpCode: Int,
        responseTimeMs: Long,
        headers: Map<String, String>? = null
    )
}

/**
 * Default implementation of NetworkLogger using Android's Log.
 */
class DefaultNetworkLogger : NetworkLogger {
    companion object {
        private const val TAG = "NetworkClient"
    }

    override fun debug(message: String, throwable: Throwable?) {
        Log.d(TAG, message, throwable)
    }

    override fun info(message: String, throwable: Throwable?) {
        Log.i(TAG, message, throwable)
    }

    override fun warning(message: String, throwable: Throwable?) {
        Log.w(TAG, message, throwable)
    }

    override fun error(message: String, throwable: Throwable?) {
        Log.e(TAG, message, throwable)
    }

    override fun logRequest(url: String, method: String, headers: Map<String, String>?) {
        val headerString = headers?.entries?.joinToString(", ") { "${it.key}=${it.value}" } ?: "None"
        debug("Request: $method $url | Headers: $headerString")
    }

    override fun logResponse(
        url: String,
        method: String,
        httpCode: Int,
        responseTimeMs: Long,
        headers: Map<String, String>?
    ) {
        val headerString = headers?.entries?.joinToString(", ") { "${it.key}=${it.value}" } ?: "None"
        info("Response: $method $url | Code: $httpCode | Time: ${responseTimeMs}ms | Headers: $headerString")
    }
}
