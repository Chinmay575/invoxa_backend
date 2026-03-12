package com.chinmaysinghmodak.invoicing.middleware

import com.chinmaysinghmodak.invoicing.dto.auth.JwtAuthenticationToken
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
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

    private val objectMapper = ObjectMapper()

    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.removePrefix("Bearer ")

        try {
            val claims = extractClaims(token)
            val authentication = JwtAuthenticationToken(
                userId = claims.subject.toLong(),
                token = token
            )
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication
            filterChain.doFilter(request, response)
        } catch (_: ExpiredJwtException) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Access token has expired. Please refresh your token.")
        } catch (_: SignatureException) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token signature.")
        } catch (_: MalformedJwtException) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Malformed token.")
        } catch (_: Exception) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token.")
        }
    }

    fun extractClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun writeErrorResponse(response: HttpServletResponse, status: Int, message: String) {
        response.status = status
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        val body = mapOf(
            "success" to false,
            "message" to message,
            "data" to null,
            "error" to message
        )
        response.writer.write(objectMapper.writeValueAsString(body))
    }
}