package com.hung.core.network

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(
        val exception: Throwable,
        val message: String? = null,
        val httpCode: Int? = null
    ) : NetworkResult<Nothing>()
}

fun <T> NetworkResult<T>.isSuccess(): Boolean = this is NetworkResult.Success
fun <T> NetworkResult<T>.isError(): Boolean = this is NetworkResult.Error
fun <T> NetworkResult<T>.getDataOrDefault(defaultValue: T): T {
    return when (this) {
        is NetworkResult.Success -> data
        else -> defaultValue
    }
}

fun <T> NetworkResult<T>.getDataOrThrow(): T {
    return when (this) {
        is NetworkResult.Success -> data
        is NetworkResult.Error -> throw exception
    }
}