package com.chinmaysinghmodak.invoicing.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "services")
data class Service(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var name: String = "",
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
)
