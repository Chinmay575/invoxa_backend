package com.chinmaysinghmodak.invoicing.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "payments")
data class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    var invoice: Invoice? = null,

    var provider: String? = null,
    var providerPaymentId: String? = null,
    var paymentMethod: String? = null,
    var amount: BigDecimal = BigDecimal.ZERO,

    @Enumerated(EnumType.STRING)
    var status: PaymentStatus = PaymentStatus.PENDING,

    var currency: String? = null,
    var failureReason: String? = null,
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
)
