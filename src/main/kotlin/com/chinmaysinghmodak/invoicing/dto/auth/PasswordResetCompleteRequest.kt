package com.chinmaysinghmodak.invoicing.dto.auth

data class PasswordResetCompleteRequest(
    var token: String = "",
    var newPassword: String = "",
)

