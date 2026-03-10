package com.chinmaysinghmodak.invoicing.dto.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    var email: String = "",
    @Min(value = 6, message = "Password must be at least 6 characters long")
    @NotBlank(message = "Password is required")
    var password: String = "",
    @NotBlank(message = "OrganizationId is required")
    var organizationId: String = "",
    @NotBlank(message = "DeviceId is required")
    var deviceId: String = "",
)

