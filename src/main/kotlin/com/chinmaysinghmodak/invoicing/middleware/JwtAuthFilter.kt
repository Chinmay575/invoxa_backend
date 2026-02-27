package com.chinmaysinghmodak.invoicing.middleware

import com.chinmaysinghmodak.invoicing.dto.JwtAuthenticationToken
import com.chinmaysinghmodak.invoicing.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.removePrefix("Bearer ")

        if (jwtService.isTokenValid(token)) {
            val claims = jwtService.extractClaims(token)
            val authentication = JwtAuthenticationToken(
                userId = claims.subject.toLong(),
                roleId = claims["roleId"]?.toString()?.toLongOrNull(),
                organizationId = claims["organizationId"]?.toString()?.toLongOrNull(),
                deviceId = claims["deviceId"]?.toString(),
                token = token
            )
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication

        }

    }
}