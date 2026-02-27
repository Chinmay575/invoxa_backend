package com.chinmaysinghmodak.invoicing.model

import jakarta.persistence.*
import java.time.Instant

@Entity
data class OrgUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    var organization: Organization? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    var role: Role? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null,

    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
)