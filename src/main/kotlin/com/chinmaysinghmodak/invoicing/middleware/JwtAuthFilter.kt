package com.chinmaysinghmodak.invoicing.middleware

import com.chinmaysinghmodak.invoicing.dto.auth.JwtAuthenticationToken
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.Base64
import javax.crypto.SecretKey

@Component
class JwtAuthFilter(
    @Value("\${jwt.secret:myDefaultSecretKeyThatIsAtLeast32BytesLongAndBase64Encoded==}") private val secret: String
) : OncePerRequestFilter() {

    private val signingKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret))
    }

    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.removePrefix("Bearer ")

        if (isTokenValid(token)) {
            val claims = extractClaims(token)
            val authentication = JwtAuthenticationToken(
                userId = claims.subject.toLong(),
                token = token
            )
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
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
}