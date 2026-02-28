package com.chinmaysinghmodak.invoicing.controller

import com.chinmaysinghmodak.invoicing.dto.ApiResponse
import com.chinmaysinghmodak.invoicing.dto.FetchOrganizationsRequest
import com.chinmaysinghmodak.invoicing.dto.JwtAuthenticationToken
import com.chinmaysinghmodak.invoicing.dto.LoginRequest
import com.chinmaysinghmodak.invoicing.dto.OrgRolesDto
import com.chinmaysinghmodak.invoicing.dto.OrgUserDto
import com.chinmaysinghmodak.invoicing.dto.PasswordResetCompleteRequest
import com.chinmaysinghmodak.invoicing.dto.PasswordResetInitiateRequest
import com.chinmaysinghmodak.invoicing.dto.RegisterRequest
import com.chinmaysinghmodak.invoicing.dto.UpdatePasswordRequest
import com.chinmaysinghmodak.invoicing.dto.UpdateProfileRequest
import com.chinmaysinghmodak.invoicing.dto.UserDto
import com.chinmaysinghmodak.invoicing.dto.orgUserToDto
import com.chinmaysinghmodak.invoicing.dto.toOrgRoleDto
import com.chinmaysinghmodak.invoicing.model.OrgUser
import com.chinmaysinghmodak.invoicing.model.User
import com.chinmaysinghmodak.invoicing.service.AuthService
import com.chinmaysinghmodak.invoicing.service.JwtService
import com.chinmaysinghmodak.invoicing.service.OrgUserService
import com.chinmaysinghmodak.invoicing.service.TokenService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User registration, login, profile & password management")
class AuthController(
    var authService: AuthService, var orgUserService: OrgUserService, var jwtService: JwtService,
    var tokenService: TokenService,
) {

    @Operation(summary = "Register a new user", description = "Creates a new user account and returns access/refresh tokens")
    @PostMapping("/register")
    fun register(@RequestBody @Valid registerRequest: RegisterRequest): ResponseEntity<ApiResponse<OrgUserDto>> {
        try {
            val user: OrgUser = authService.register(registerRequest)

            val accessToken = jwtService.generateAccessToken(user, registerRequest.deviceId)
            val refreshToken = jwtService.generateRefreshToken(user, registerRequest.deviceId)

            tokenService.saveRefreshToken(refreshToken, user, registerRequest.deviceId)

            return ResponseEntity.ok(
                ApiResponse(
                    success = true,
                    data = orgUserToDto(user, accessToken, refreshToken),
                    message = "User registered successfully"
                )
            )
        } catch (e: Exception) {
            return ResponseEntity.ok(
                ApiResponse(
                    success = false, error = e.message, message = "User registration failed"
                )
            )
        }

    }


    @Operation(summary = "Fetch user organizations", description = "Returns the list of organizations a user belongs to, given valid credentials")
    @PostMapping("/organizations")
    fun fetchOrganizations(@Valid @RequestBody request: FetchOrganizationsRequest): ResponseEntity<ApiResponse<List<OrgRolesDto>>> {

        val user: User? = authService.getUserByEmail(request.email, request.password);

        if (user != null) {
            val orgUsers: List<OrgUser>? = orgUserService.getOrgUserByUserId(user)
            return ResponseEntity.ok(
                ApiResponse(
                    success = true, data = orgUsers?.map {
                        toOrgRoleDto(it)
                    } ?: emptyList(), message = "Organizations fetched successfully"
                )
            );
        } else {
            println("User not found with email: ${request.email}")
        }

        return ResponseEntity.ok(
            ApiResponse(
                success = false, error = "Invalid credentials", message = "Failed to fetch organizations"
            )
        );
    }

    @Operation(summary = "Login", description = "Authenticates a user with email, password, and organization, returning access/refresh tokens")
    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequest): ResponseEntity<ApiResponse<OrgUserDto>> {

        try {
            val user: OrgUser? = authService.login(request.email, request.password, request.organizationId);

            if (user != null) {
                val accessToken = jwtService.generateAccessToken(user, request.deviceId)
                val refreshToken = jwtService.generateRefreshToken(user, request.deviceId)

                tokenService.saveRefreshToken(refreshToken, user, request.deviceId)

                return ResponseEntity.ok(
                    ApiResponse(
                        success = true,
                        data = orgUserToDto(user, accessToken, refreshToken),
                        message = "Login successfully"
                    )
                )
            }
        } catch (e: Exception) {
            println("Error during login: ${e.message}")
            return ResponseEntity.ok(
                ApiResponse(
                    success = false, error = e.message, message = "Login failed"
                )
            )

        }

        return ResponseEntity.ok(
            ApiResponse(
                success = false, error = "Invalid credentials", message = "Login failed"
            )
        )
    }

    @Operation(summary = "Logout", description = "Revokes the current refresh token and clears the security context")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/logout")
    fun logout() {
        val auth = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken

        tokenService.revokeToken(auth.token)

        SecurityContextHolder.clearContext()
    }

    @Operation(summary = "Logout from all devices", description = "Revokes all refresh tokens for the current user")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/logout/all")
    fun logoutFromAllDevices() {
        val auth = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken

        tokenService.revokeAllTokensByUser(auth.userId)

        SecurityContextHolder.clearContext()
    }

    @Operation(summary = "Get user profile", description = "Returns the profile of the currently authenticated user")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/profile")
    fun getProfile(): ResponseEntity<ApiResponse<UserDto>> {

        val auth = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken

        val user = authService.getProfile(auth.userId) ?: return ResponseEntity.status(404).body(
            ApiResponse(
                success = false, error = "User not found", message = "Failed to fetch profile"
            )
        )

        return ResponseEntity.ok(
            ApiResponse(
                success = true, data = user, message = "Profile fetched successfully"
            )
        )
    }

    @Operation(summary = "Update user profile", description = "Updates profile fields (fullName, mobile, username, profilePic) for the authenticated user")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/profile")
    fun updateUser(@RequestBody @Valid request: UpdateProfileRequest): ResponseEntity<ApiResponse<String>> {
        val auth = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken

        return try {
            authService.updateProfile(
                userId = auth.userId,
                fullName = request.fullName,
                mobile = request.mobile,
                username = request.username,
                profilePic = request.profilePic,
            )
            ResponseEntity.ok(ApiResponse(success = true, message = "Profile updated successfully"))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(success = false, error = e.message, message = "Failed to update profile"))
        }
    }

    @Operation(summary = "Verify email", description = "Verifies a user's email address using the token sent via email")
    @PostMapping("/email/verify")
    fun verifyEmail(@RequestParam token: String): ResponseEntity<ApiResponse<String>> {
        return try {
            authService.verifyEmail(token)
            ResponseEntity.ok(ApiResponse(success = true, message = "Email verified successfully"))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(success = false, error = e.message, message = "Email verification failed"))
        }
    }

    @Operation(summary = "Update password", description = "Changes the password for the authenticated user")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/password/update")
    fun updatePassword(@RequestBody @Valid request: UpdatePasswordRequest): ResponseEntity<ApiResponse<String>> {
        val auth = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken

        try {
            authService.updatePassword(request.currentPassword!!, request.newPassword!!, auth.userId)

            return ResponseEntity.ok(
                ApiResponse(
                    success = true, message = "Password updated successfully"
                )
            )
        } catch (e: Exception) {
            println("Error updating password: ${e.message}")
            return ResponseEntity.ok(
                ApiResponse(
                    success = false, error = e.message, message = "Failed to update password"
                )
            )
        }
    }

    @Operation(summary = "Initiate password reset", description = "Sends a password reset link to the user's email if the account exists")
    @PostMapping("/password/reset/initiate")
    fun passwordResetInitiate(@RequestBody @Valid request: PasswordResetInitiateRequest): ResponseEntity<ApiResponse<String>> {
        return try {
            authService.passwordResetInitiate(request.email ?: "")
            // Always return success to avoid leaking whether an email exists
            ResponseEntity.ok(ApiResponse(success = true, message = "If an account with that email exists, a reset link has been sent"))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(success = false, error = e.message, message = "Failed to initiate password reset"))
        }
    }

    @Operation(summary = "Complete password reset", description = "Resets the user's password using the token from the reset email")
    @PostMapping("/password/reset/complete")
    fun passwordResetComplete(@RequestBody @Valid request: PasswordResetCompleteRequest): ResponseEntity<ApiResponse<String>> {
        return try {
            authService.passwordResetComplete(request.token, request.newPassword)
            ResponseEntity.ok(ApiResponse(success = true, message = "Password reset successfully"))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(success = false, error = e.message, message = "Password reset failed"))
        }
    }

}