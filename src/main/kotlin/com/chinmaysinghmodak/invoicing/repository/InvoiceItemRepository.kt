package com.chinmaysinghmodak.invoicing.repository

import com.chinmaysinghmodak.invoicing.model.InvoiceItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InvoiceItemRepository : JpaRepository<InvoiceItem, Long> {
}