package com.chinmaysinghmodak.invoicing.dto

data class UpdatePasswordRequest (
    var currentPassword: String? = null,
    var newPassword: String? = null
)