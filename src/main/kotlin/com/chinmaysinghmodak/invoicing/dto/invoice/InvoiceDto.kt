package com.chinmaysinghmodak.invoicing.dto.invoice

import com.chinmaysinghmodak.invoicing.model.Invoice
import com.chinmaysinghmodak.invoicing.model.InvoiceStatus
import java.math.BigDecimal

data class InvoiceDto(
    var id: Long? = 0,
    var invoiceNumber: String? = null,
    var customerName: String? = null,
    var amount: BigDecimal? = null,
    var status: InvoiceStatus? = null,
)

fun toInvoiceDto(invoice: Invoice?): InvoiceDto {
    return InvoiceDto(
        id = invoice?.id,
        invoiceNumber = invoice?.invoiceNumber,
        customerName = invoice?.customer?.name,
        amount = invoice?.totalAmount,
        status = invoice?.status
    )
}

