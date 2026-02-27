package com.chinmaysinghmodak.invoicing.middleware

import org.springframework.context.event.EventListener
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class EventListeners(
    private val mailSender: JavaMailSender,
) {

    @Async
    @EventListener
    fun welcomeUserListener(event: UserRegisteredEvent) {
        val user = event.user
        val to = user.email ?: return

        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setTo(to)
        helper.setSubject("Welcome to Invoicing!")
        helper.setText(
            """
            <h2>Welcome${user.fullName?.let { ", $it" } ?: ""}!</h2>
            <p>Your account has been created successfully.</p>
            <p>You can now start managing your invoices.</p>
            """.trimIndent(),
            true
        )
        mailSender.send(message)
    }

    @Async
    @EventListener
    fun emailVerificationListener(event: UserRegisteredEvent) {
        val user = event.user
        val to = user.email ?: return
        val token = event.verificationToken ?: return

        val verifyLink = "http://localhost:8080/auth/email/verify?token=$token"

        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setTo(to)
        helper.setSubject("Verify your email address")
        helper.setText(
            """
            <h2>Email Verification</h2>
            <p>Please verify your email address by clicking the link below:</p>
            <a href="$verifyLink">Verify Email</a>
            <p>This link will expire in 24 hours.</p>
            <p>If you did not create an account, please ignore this email.</p>
            """.trimIndent(),
            true
        )
        mailSender.send(message)
    }

    @Async
    @EventListener
    fun passwordResetListener(event: PasswordResetRequestedEvent) {
        val user = event.user
        val to = user.email ?: return

        val resetLink = "http://localhost:8080/auth/password/reset/complete?token=${event.resetToken}"

        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setTo(to)
        helper.setSubject("Reset your password")
        helper.setText(
            """
            <h2>Password Reset</h2>
            <p>We received a request to reset your password. Click the link below to proceed:</p>
            <a href="$resetLink">Reset Password</a>
            <p>This link will expire in 1 hour.</p>
            <p>If you did not request a password reset, please ignore this email.</p>
            """.trimIndent(),
            true
        )
        mailSender.send(message)
    }
}
