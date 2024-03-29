package com.example.ktor.exception

import java.io.IOException

class ApiCallException {

    sealed class ApiCallException(message: String) : IOException(message)

    class ApiExceptions(message: String) : ApiCallException(message)

    class TimeoutException(message: String) : ApiCallException(message)

    class NetworkException(message: String) : ApiCallException(message)

    class UnauthorizedUserException(message: String) : IOException(message)
    class NotFoundException(message: String) : ApiCallException(message)
}