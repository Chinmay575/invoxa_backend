package com.chinmaysinghmodak.invoicing.model

import jakarta.persistence.*
import java.time.Instant

@Entity
data class PendingVerification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null,

    var type: String = "",
    var uniqueToken: String = "",
    var createdAt: Instant = Instant.now(),
    var expiryAt: Instant? = null,
    var updatedAt: Instant = Instant.now(),
)