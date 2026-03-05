package com.chinmaysinghmodak.invoicing.service

import com.chinmaysinghmodak.invoicing.dto.CreateInvoiceRequest
import com.chinmaysinghmodak.invoicing.dto.InvoiceDto
import com.chinmaysinghmodak.invoicing.dto.toInvoiceDto
import com.chinmaysinghmodak.invoicing.model.Customer
import com.chinmaysinghmodak.invoicing.model.Invoice
import com.chinmaysinghmodak.invoicing.model.OrgUser
import com.chinmaysinghmodak.invoicing.repository.CustomerRepository
import com.chinmaysinghmodak.invoicing.repository.InvoiceRepository
import com.chinmaysinghmodak.invoicing.repository.OrgUserRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class InvoiceService(
    private val orgUserRepository: OrgUserRepository,
    private val customerRepository: CustomerRepository,
    private val invoiceRepository: InvoiceRepository
) {

    fun createInvoice(request: CreateInvoiceRequest, userId: Long): InvoiceDto {


        try {


            val orgUser: OrgUser =
                orgUserRepository.findById(userId).orElseThrow { Exception("User does not exist") }


            val customer: Customer =
                customerRepository.findById(request.customer).orElseThrow { Exception("Customer not found") }

            val newInv = Invoice(

                organization = orgUser.organization,
                createdAt = Instant.now(),
                status = request.status,
                invoiceNumber = request.invoiceNumber,
                dueDate = request.dueDate,
                issueDate = request.issueDate,
                createdByOrgUser = orgUser, customer = customer,
                subTotal = request.subTotal,
                taxAmount = request.taxAmount,
                updatedAt = Instant.now(),
                totalAmount = request.totalAmount,

                )

            val invoice = invoiceRepository.save(newInv)

            return toInvoiceDto(invoice)

        } catch (e: Exception) {
            throw e
        }

    }

    fun getAllInvoices(userId: Long): List<InvoiceDto> {
        try {
            val orgUser: OrgUser =
                orgUserRepository.findById(userId).orElseThrow { Exception("User does not exist") }

            val invoices = invoiceRepository.findAllByOrganization(orgUser.organization)

            return invoices.map { toInvoiceDto(it) }
        } catch (e: Exception) {
            throw e
        }
    }

    fun getInvoiceById(id: Long, userId: Long): InvoiceDto {
        try {
            val orgUser: OrgUser =
                orgUserRepository.findById(userId).orElseThrow { Exception("User does not exist") }

            val invoice = invoiceRepository.findByIdAndOrganization(id, orgUser.organization)
                ?: throw Exception("Invoice not found")

            return toInvoiceDto(invoice)
        } catch (e: Exception) {
            throw e
        }
    }

}