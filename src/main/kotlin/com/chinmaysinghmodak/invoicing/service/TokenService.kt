package com.chinmaysinghmodak.invoicing.service

import com.chinmaysinghmodak.invoicing.dto.auth.TokenRefreshResponse
import com.chinmaysinghmodak.invoicing.model.OrgUser
import com.chinmaysinghmodak.invoicing.model.Token
import com.chinmaysinghmodak.invoicing.model.User
import com.chinmaysinghmodak.invoicing.repository.AuthRepository
import com.chinmaysinghmodak.invoicing.repository.OrgUserRepository
import com.chinmaysinghmodak.invoicing.repository.TokenRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class TokenService(
    private val authRepo: AuthRepository,
    private val tokenRepository: TokenRepository,
    private val jwtService: JwtService,
    private val orgUserRepository: OrgUserRepository,
) {

    @Transactional
    fun refreshAccessToken(refreshToken: String, deviceId: String): TokenRefreshResponse {
        // 1. Validate JWT signature and expiry
        if (!jwtService.isTokenValid(refreshToken)) {
            throw Exception("Invalid or expired refresh token")
        }

        // 2. Ensure it's a REFRESH token, not an ACCESS token
        val tokenType = jwtService.getTokenType(refreshToken)
        if (tokenType != "REFRESH") {
            throw Exception("Provided token is not a refresh token")
        }

        // 3. Check the token exists in DB and is not revoked
        val storedToken = tokenRepository.findByTokenHashAndRevokedFalse(refreshToken)
            ?: throw Exception("Refresh token has been revoked or does not exist")

        // 4. Verify device ID matches
        if (storedToken.deviceId != deviceId) {
            throw Exception("Device ID mismatch")
        }

        // 5. Look up the OrgUser from the token subject
        val orgUserId = jwtService.getSubject(refreshToken)
            ?: throw Exception("Invalid token subject")

        val orgUser = orgUserRepository.findById(orgUserId)
            .orElseThrow { Exception("User not found") }

        // 6. Revoke old refresh token
        storedToken.revoked = true
        storedToken.updatedAt = Instant.now()
        tokenRepository.save(storedToken)

        // 7. Generate new access + refresh tokens (token rotation)
        val newAccessToken = jwtService.generateAccessToken(orgUser)
        val newRefreshToken = jwtService.generateRefreshToken(orgUser)

        // 8. Save the new refresh token
        saveRefreshToken(newRefreshToken, orgUser, deviceId)

        return TokenRefreshResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
        )
    }

    @Transactional
    fun saveRefreshToken(token: String, user: OrgUser, deviceId: String) {
        try {


            val tokenEntity = Token(
                tokenHash = token,
                user = user.user,
                deviceId = deviceId,
                expiryAt = jwtService.getRefreshTokenExpiryDate()
            )
            tokenRepository.save(tokenEntity)
        } catch (e: Exception) {
            throw e
        }
    }

    @Transactional
    fun rotateRefreshToken(oldTokenHash: String, orgUser: OrgUser, deviceId: String): String {
        revokeToken(oldTokenHash)
        val newToken = jwtService.generateRefreshToken(orgUser)
        saveRefreshToken(newToken, orgUser, deviceId)
        return newToken
    }

    @Transactional
    fun revokeToken(tokenHash: String) {
        try {
            val token = tokenRepository.findByTokenHashAndRevokedFalse(tokenHash)
            if (token != null) {
                token.revoked = true
                token.updatedAt = Instant.now()
                tokenRepository.save(token)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    @Transactional
    fun revokeAllTokensByUser(userId: Long): Int {

        try {
            val user: User = authRepo.findById(userId).orElse(null) ?: throw Exception("User not found")

            return tokenRepository.revokeAllTokensByUser(user)

        } catch (e: Exception) {
            throw e
        }
    }

    @Transactional
    fun revokeAllTokensByUserAndDevice(user: User, deviceId: String): Int {
        return tokenRepository.revokeAllTokensByUserAndDevice(user, deviceId)
    }
}