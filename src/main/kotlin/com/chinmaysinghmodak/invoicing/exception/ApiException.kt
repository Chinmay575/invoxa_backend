package com.chinmaysinghmodak.chefio_backend.exception

import org.springframework.http.HttpStatus


class ApiException(
    val code: String,
    override val message: String,
    val status: HttpStatus
) : RuntimeException(message)
