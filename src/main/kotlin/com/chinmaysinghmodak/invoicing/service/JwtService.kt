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

    fun generateAccessToken(user: OrgUser): String {
        return Jwts.builder()
            .setSubject(user.id.toString())
            .claim("type", "ACCESS")
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + accessTokenValidityMs))
            .signWith(signingKey)
            .compact()
    }

    fun generateRefreshToken(user: OrgUser): String {
        return Jwts.builder()
            .setSubject(user.id.toString())
            .claim("type", "REFRESH")
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + refreshTokenValidityMs))
            .signWith(signingKey)
            .compact()
    }

    fun getRefreshTokenExpiryDate(): Instant {
        return Instant.now().plusMillis(refreshTokenValidityMs)
    }

    fun extractClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun isTokenValid(token: String): Boolean {
        return try {
            extractClaims(token)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun getTokenType(token: String): String? {
        return try {
            extractClaims(token).get("type", String::class.java)
        } catch (_: Exception) {
            null
        }
    }

    fun getSubject(token: String): Long? {
        return try {
            extractClaims(token).subject.toLong()
        } catch (_: Exception) {
            null
        }
    }
}