package com.chinmaysinghmodak.invoicing.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "invoice_items")
data class InvoiceItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    var invoice: Invoice? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    var product: Product? = null,

    var description: String? = null,
    var quantity: Int = 0,
    var unitPrice: BigDecimal = BigDecimal.ZERO,
    var lineTotal: BigDecimal = BigDecimal.ZERO,
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
)
