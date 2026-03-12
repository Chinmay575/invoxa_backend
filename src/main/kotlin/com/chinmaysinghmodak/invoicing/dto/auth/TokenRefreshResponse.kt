package com.chinmaysinghmodak.invoicing.dto.auth

data class TokenRefreshResponse(
    val accessToken: String,
    val refreshToken: String,
)

