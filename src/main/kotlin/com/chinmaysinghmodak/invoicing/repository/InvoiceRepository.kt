package com.chinmaysinghmodak.invoicing.repository

import com.chinmaysinghmodak.invoicing.model.Invoice
import com.chinmaysinghmodak.invoicing.model.Organization
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InvoiceRepository : JpaRepository<Invoice, Long> {
    fun findAllByOrganization(organization: Organization?) : List<Invoice>;
    fun findByIdAndOrganization(id: Long, organization: Organization?) : Invoice?;
}