package com.chinmaysinghmodak.invoicing.service

import com.chinmaysinghmodak.invoicing.dto.RegisterRequest
import com.chinmaysinghmodak.invoicing.dto.UserDto
import com.chinmaysinghmodak.invoicing.dto.userToDto
import com.chinmaysinghmodak.invoicing.exception.UserNotFound
import com.chinmaysinghmodak.invoicing.middleware.UserRegisteredEvent
import com.chinmaysinghmodak.invoicing.middleware.PasswordResetRequestedEvent
import com.chinmaysinghmodak.invoicing.model.OrgUser
import com.chinmaysinghmodak.invoicing.model.PendingVerification
import com.chinmaysinghmodak.invoicing.model.User
import com.chinmaysinghmodak.invoicing.repository.AuthRepository
import com.chinmaysinghmodak.invoicing.repository.PendingVerificationRepository
import jakarta.transaction.Transactional
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Optional
import java.util.UUID

@Service
class AuthService(
    var authRepository: AuthRepository,
    var organizationService: OrganizationService,
    var orgUserService: OrgUserService,
    var roleService: RoleService,
    var passwordEncoder: PasswordEncoder,
    var pendingVerificationRepository: PendingVerificationRepository,
    var eventPublisher: ApplicationEventPublisher,
) {

    fun getUserByEmail(email: String, password: String): User? {
        val user = authRepository.findByEmail(email)

        return if (user != null) {
            if (passwordEncoder.matches(password, user.password)) user else null
        } else {
            null
        }
    }

    @Transactional
    fun register(registerRequest: RegisterRequest): OrgUser {
        val req = User(
            email = registerRequest.email,
            password = passwordEncoder.encode(registerRequest.password),
        )

        val user = authRepository.save(req)

        val organization = organizationService.createOrganization("My Organization")
        val role = roleService.createRole("Owner", organization)
        val orgUser = orgUserService.createOrgUser(user, organization, role)

        // Create email-verification token
        val token = UUID.randomUUID().toString()
        val verification = PendingVerification(
            user = user,
            type = "EMAIL_VERIFICATION",
            uniqueToken = token,
            expiryAt = Instant.now().plusSeconds(86400), // 24 hours
        )
        pendingVerificationRepository.save(verification)

        // Publish registration event — listeners send welcome + verification emails asynchronously
        eventPublisher.publishEvent(UserRegisteredEvent(user = user, verificationToken = token))

        return orgUser
    }

    fun login(email: String, password: String, organizationId: String): OrgUser? {
        val user = authRepository.findByEmail(email)

        return if (user != null) {
            if (passwordEncoder.matches(password, user.password)) {
                val orgUser = orgUserService.getOrgUserByUserId(user)
                orgUser?.firstOrNull { it.organization?.id.toString() == organizationId }
            } else {
                null
            }
        } else {
            null
        }
    }

    fun getProfile(id: Long): UserDto? {
        val result: Optional<User> = authRepository.findById(id)

        return if (result.isPresent) {
            userToDto(result.get())
        } else {
            throw UserNotFound()
        }
    }

    @Transactional
    fun updateProfile(userId: Long, fullName: String?, mobile: String?, username: String?, profilePic: String?) {
        val user = authRepository.findById(userId).orElseThrow { UserNotFound() }

        fullName?.let { user.fullName = it }
        mobile?.let { user.mobile = it }
        username?.let { user.username = it }
        profilePic?.let { user.profilePic = it }
        user.updatedAt = Instant.now()

        authRepository.save(user)
    }

    @Transactional
    fun verifyEmail(token: String) {
        val verification = pendingVerificationRepository.findByUniqueToken(token)
            ?: throw Exception("Invalid or expired verification token")

        if (verification.type != "EMAIL_VERIFICATION") {
            throw Exception("Invalid token type")
        }

        if (verification.expiryAt != null && verification.expiryAt!!.isBefore(Instant.now())) {
            pendingVerificationRepository.delete(verification)
            throw Exception("Verification token has expired. Please request a new one.")
        }

        val user = verification.user ?: throw Exception("User not found")
        user.isEmailVerified = true
        user.updatedAt = Instant.now()
        authRepository.save(user)

        pendingVerificationRepository.delete(verification)
    }

    @Transactional
    fun updatePassword(password: String, newPassword: String, userId: Long) {
        val user = authRepository.findById(userId)

        if (user.isPresent) {
            val existingUser = user.get()

            if (passwordEncoder.matches(password, existingUser.password)) {
                existingUser.password = passwordEncoder.encode(newPassword)
                existingUser.updatedAt = Instant.now()
                authRepository.save(existingUser)
            } else {
                throw Exception("Current password is incorrect")
            }
        } else {
            throw UserNotFound()
        }
    }

    @Transactional
    fun passwordResetInitiate(email: String) {
        val user = authRepository.findByEmail(email) ?: return // don't reveal whether email exists

        // Remove any existing reset token for this user
        pendingVerificationRepository.findByUserAndType(user, "PASSWORD_RESET")
            ?.let { pendingVerificationRepository.delete(it) }

        val token = UUID.randomUUID().toString()
        val verification = PendingVerification(
            user = user,
            type = "PASSWORD_RESET",
            uniqueToken = token,
            expiryAt = Instant.now().plusSeconds(3600), // 1 hour
        )
        pendingVerificationRepository.save(verification)

        // Send password-reset email asynchronously via event
        eventPublisher.publishEvent(PasswordResetRequestedEvent(user = user, resetToken = token))
    }

    @Transactional
    fun passwordResetComplete(token: String, newPassword: String) {
        val verification = pendingVerificationRepository.findByUniqueToken(token)
            ?: throw Exception("Invalid or expired password reset token")

        if (verification.type != "PASSWORD_RESET") {
            throw Exception("Invalid token type")
        }

        if (verification.expiryAt != null && verification.expiryAt!!.isBefore(Instant.now())) {
            pendingVerificationRepository.delete(verification)
            throw Exception("Password reset token has expired. Please request a new one.")
        }

        val user = verification.user ?: throw Exception("User not found")
        user.password = passwordEncoder.encode(newPassword)
        user.updatedAt = Instant.now()
        authRepository.save(user)

        pendingVerificationRepository.delete(verification)
    }
}


