package com.chinmaysinghmodak.invoicing.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "role_permissions")
@IdClass(RolePermissionId::class)
data class RolePermission(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    var role: Role? = null,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    var permission: Permission? = null,

    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
)
