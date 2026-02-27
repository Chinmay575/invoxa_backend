package com.chinmaysinghmodak.invoicing.service

import com.chinmaysinghmodak.invoicing.model.OrgUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Base64
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${jwt.secret}") private val secret: String
) {

    private val signingKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret))
    }

    private val accessTokenValidityMs = 15 * 60 * 1000
    private val refreshTokenValidityMs = 30L * 24 * 60 * 60 * 1000

    fun generateAccessToken(user: OrgUser, deviceId: String): String {
        return Jwts.builder()
            .setSubject(user.id.toString())
            .claim("roleId", user.role?.id)
            .claim("organizationId", user.organization?.id)
            .claim("deviceId", deviceId)
            .claim("type", "ACCESS")
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + accessTokenValidityMs))
            .signWith(signingKey)
            .compact()
    }

    fun generateRefreshToken(user: OrgUser, deviceId: String): String {
        return Jwts.builder()
            .setSubject(user.id.toString())
            .claim("roleId", user.role?.id)
            .claim("organizationId", user.organization?.id)
            .claim("deviceId", deviceId)
            .claim("type", "REFRESH")
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + refreshTokenValidityMs))
            .signWith(signingKey)
            .compact()
    }

    fun extractClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun extractUserId(token: String): Long {
        return extractClaims(token).subject.toLong()
    }

    fun extractRoleId(token: String): Long? {
        val value = extractClaims(token)["roleId"] ?: return null
        return (value as Number).toLong()
    }

    fun extractOrganizationId(token: String): Long? {
        val value = extractClaims(token)["organizationId"] ?: return null
        return (value as Number).toLong()
    }

    fun extractTokenType(token: String): String {
        return extractClaims(token)["type"] as String
    }

    fun isTokenValid(token: String): Boolean {
        return try {
            extractClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getRefreshTokenExpiryDate(): Instant {
        return Instant.now().plusMillis(refreshTokenValidityMs)
    }
}