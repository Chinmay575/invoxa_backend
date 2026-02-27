package com.chinmaysinghmodak.invoicing.dto

data class LoginRequest(
    var email: String = "",
    var password: String = "",
    var organizationId: String = "",
    var deviceId: String = "",
)