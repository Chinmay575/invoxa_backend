package com.chinmaysinghmodak.invoicing.dto

data class RegisterRequest (
    var email: String = "",
    var password: String = "",
    var deviceId: String = "",
)