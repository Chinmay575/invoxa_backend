package com.chinmaysinghmodak.invoicing.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "plans")
data class Plan(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var name: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    var service: Service? = null,

    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),

    @Enumerated(EnumType.STRING)
    var billingCycle: BillingCycle = BillingCycle.MONTHLY,

    var price: BigDecimal = BigDecimal.ZERO,
)
