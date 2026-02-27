package com.chinmaysinghmodak.invoicing.dto

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(
    val userId: Long,
    val roleId: Long?,
    val organizationId: Long?,
    val deviceId: String? = null,
    val token: String,
    authorities: Collection<GrantedAuthority> = emptyList()
) : AbstractAuthenticationToken(authorities) {

    init {
        isAuthenticated = true
    }

    override fun getCredentials(): Any? = null
    override fun getPrincipal(): Any = userId
}
