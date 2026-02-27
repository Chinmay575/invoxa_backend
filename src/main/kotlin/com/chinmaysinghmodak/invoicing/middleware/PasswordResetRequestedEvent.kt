package com.chinmaysinghmodak.invoicing.middleware

import com.chinmaysinghmodak.invoicing.model.User

data class PasswordResetRequestedEvent(
    val user: User,
    val resetToken: String,
)

