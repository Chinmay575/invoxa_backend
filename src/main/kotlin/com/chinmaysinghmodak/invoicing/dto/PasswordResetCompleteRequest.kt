package com.chinmaysinghmodak.invoicing.dto

data class PasswordResetCompleteRequest(
    var token: String = "",
    var newPassword: String = "",
)

