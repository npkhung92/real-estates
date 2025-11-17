package com.hung.core.domain.exception
abstract class DomainException(override val message: String, open val throwable: Throwable) :
    Exception(message, throwable) {
    constructor(cause: Throwable) : this(cause.message.orEmpty(), cause)
    constructor(message: String) : this(Exception(message))
}
