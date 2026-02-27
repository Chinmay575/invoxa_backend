package com.chinmaysinghmodak.invoicing.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "customers")
data class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var name: String = "",
    var email: String? = null,
    var address: String? = null,
    var mobile: String? = null,
    var taxNumber: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    var organization: Organization? = null,

    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
)
