package com.chinmaysinghmodak.invoicing.model

import jakarta.persistence.*
import java.time.Instant

@Entity
data class Token(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var tokenHash: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null,

    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
    var expiryAt: Instant? = null,
    var revoked: Boolean = false,
    var deviceId: String? = null,
)