package com.chinmaysinghmodak.invoicing.dto.auth

data class UpdatePasswordRequest (
    var currentPassword: String? = null,
    var newPassword: String? = null
)

