package com.chinmaysinghmodak.invoicing.dto

import com.chinmaysinghmodak.invoicing.model.InvoiceStatus
import java.math.BigDecimal
import java.time.Instant

data class CreateInvoiceRequest (
    var customer: Long? = null,
    var invoiceNumber: String? = null,
    var taxAmount: BigDecimal = BigDecimal.ZERO,
    var subTotal: BigDecimal = BigDecimal.ZERO,
    var totalAmount: BigDecimal = BigDecimal.ZERO,
    var currency: String? = null,
    var issueDate: Instant? = null,
    var dueDate: Instant? = null,
    var status: InvoiceStatus = InvoiceStatus.DRAFT,
    var items : List<Item> = emptyList(),
    )

data class Item (
    var id : Long,
    var description: String? = null,
    var quantity: Int = 0,
    var unitPrice: BigDecimal = BigDecimal.ZERO,
    var totalPrice: BigDecimal = BigDecimal.ZERO,
)