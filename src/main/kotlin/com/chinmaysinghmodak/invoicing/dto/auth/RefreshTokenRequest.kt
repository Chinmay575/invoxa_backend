package com.chinmaysinghmodak.invoicing.dto.auth

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequest(
    @field:NotBlank(message = "Refresh token is required")
    val refreshToken: String,

    @field:NotBlank(message = "Device ID is required")
    val deviceId: String,
)

