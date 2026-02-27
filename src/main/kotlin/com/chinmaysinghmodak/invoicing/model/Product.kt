package com.chinmaysinghmodak.invoicing.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "products")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var name: String = "",
    var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    var organization: Organization? = null,

    var unitPrice: BigDecimal = BigDecimal.ZERO,
    var stock: Int = 0,
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
)
