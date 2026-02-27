package com.chinmaysinghmodak.invoicing.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity
data class Organization(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var name: String = "",
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
)