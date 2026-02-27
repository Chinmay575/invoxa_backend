package com.chinmaysinghmodak.invoicing.middleware

import com.chinmaysinghmodak.invoicing.model.User

data class UserRegisteredEvent(
    val user: User,
    val verificationToken: String? = null,
)


