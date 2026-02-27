package com.chinmaysinghmodak.invoicing.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "invoices")
data class Invoice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    var organization: Organization? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    var customer: Customer? = null,

    var invoiceNumber: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_org_user_id")
    var createdByOrgUser: OrgUser? = null,

    var taxAmount: BigDecimal = BigDecimal.ZERO,
    var subTotal: BigDecimal = BigDecimal.ZERO,
    var totalAmount: BigDecimal = BigDecimal.ZERO,
    var currency: String? = null,
    var issueDate: Instant? = null,
    var dueDate: Instant? = null,

    @Enumerated(EnumType.STRING)
    var status: InvoiceStatus = InvoiceStatus.DRAFT,

    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
)
