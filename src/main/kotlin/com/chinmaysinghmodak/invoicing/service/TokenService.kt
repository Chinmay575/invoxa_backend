package com.chinmaysinghmodak.invoicing.service

import com.chinmaysinghmodak.invoicing.model.OrgUser
import com.chinmaysinghmodak.invoicing.model.Token
import com.chinmaysinghmodak.invoicing.model.User
import com.chinmaysinghmodak.invoicing.repository.AuthRepository
import com.chinmaysinghmodak.invoicing.repository.TokenRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class TokenService(
    private val authRepo: AuthRepository,
    private val tokenRepository: TokenRepository,
    private val jwtService: JwtService,
) {

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