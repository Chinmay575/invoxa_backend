package com.chinmaysinghmodak.invoicing.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "memberships")
data class Membership(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    var organization: Organization? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    var plan: Plan? = null,

    @Enumerated(EnumType.STRING)
    var status: MembershipStatus = MembershipStatus.ACTIVE,

    var createdAt: Instant = Instant.now(),
    var expiryAt: Instant? = null,
    var updatedAt: Instant = Instant.now(),
)
