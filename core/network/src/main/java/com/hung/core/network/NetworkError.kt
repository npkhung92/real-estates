package com.hung.core.network

import retrofit2.HttpException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class NetworkError : Exception() {
    /**
     * Network connectivity issues (no internet, timeout, etc.)
     */
    data class ConnectivityError(
        override val message: String,
        override val cause: Throwable? = null
    ) : NetworkError()
    
    /**
     * HTTP error responses (4xx status codes)
     */
    data class HttpError(
        val httpCode: Int,
        override val message: String,
        val responseBody: String? = null
    ) : NetworkError()
    
    /**
     * Server errors (5xx status codes)
     */
    data class ServerError(
        override val message: String,
        val httpCode: Int,
        val responseBody: String? = null
    ) : NetworkError()
    
    /**
     * Unknown or unexpected errors
     */
    data class UnknownError(
        override val message: String,
        override val cause: Throwable? = null
    ) : NetworkError()
}

fun Throwable.toNetworkError(): NetworkError = when (this) {
    is NetworkError -> this
    is UnknownHostException, is ConnectException -> NetworkError.ConnectivityError(
        message = "Unable to resolve host. Please check your internet connection.",
        cause = this
    )
    is SocketTimeoutException -> NetworkError.ConnectivityError(
        message = "Request timed out. Please try again.",
        cause = this
    )
    is HttpException -> {
        val httpCode = this.code()
        val responseBody = this.response()?.errorBody()?.string()
        when (httpCode) {
            in HttpURLConnection.HTTP_BAD_REQUEST..499 -> {
                NetworkError.HttpError(
                    httpCode = httpCode,
                    message = "Client error: $httpCode",
                    responseBody = responseBody
                )
            }
            in HttpURLConnection.HTTP_INTERNAL_ERROR..599 -> NetworkError.ServerError(
                httpCode = httpCode,
                message = "Server error: $httpCode",
                responseBody = responseBody
            )
            else -> NetworkError.UnknownError(
                message = "HTTP error: $httpCode",
                cause = this
            )
        }
    }
    else -> NetworkError.UnknownError(
        message = this.message ?: "An unknown error occurred",
        cause = this
    )
}
