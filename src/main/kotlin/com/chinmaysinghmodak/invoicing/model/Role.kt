package com.chinmaysinghmodak.invoicing.model

import jakarta.persistence.*
import java.time.Instant

@Entity
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var name: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    var organization: Organization? = null,

    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
)