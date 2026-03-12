package com.chinmaysinghmodak.invoicing.service

import com.chinmaysinghmodak.invoicing.dto.invoice.CreateInvoiceRequest
import com.chinmaysinghmodak.invoicing.dto.invoice.InvoiceDto
import com.chinmaysinghmodak.invoicing.dto.invoice.toInvoiceDto
import com.chinmaysinghmodak.invoicing.config.CacheConfig
import com.chinmaysinghmodak.invoicing.model.Customer
import com.chinmaysinghmodak.invoicing.model.Invoice
import com.chinmaysinghmodak.invoicing.model.InvoiceItem
import com.chinmaysinghmodak.invoicing.model.OrgUser
import com.chinmaysinghmodak.invoicing.repository.CustomerRepository
import com.chinmaysinghmodak.invoicing.repository.InvoiceItemRepository
import com.chinmaysinghmodak.invoicing.repository.InvoiceRepository
import com.chinmaysinghmodak.invoicing.repository.OrgUserRepository
import com.chinmaysinghmodak.invoicing.repository.ProductRepository
import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class InvoiceService(
    private val orgUserRepository: OrgUserRepository,
    private val customerRepository: CustomerRepository,
    private val invoiceRepository: InvoiceRepository,
    private val invoiceItemRepository: InvoiceItemRepository,
    private val productRepository: ProductRepository
) {

    @CacheEvict(value = [CacheConfig.INVOICES], key = "#userId")
    @Transactional
    fun createInvoice(request: CreateInvoiceRequest, userId: Long): InvoiceDto {


        try {


            val orgUser: OrgUser =
                orgUserRepository.findById(userId).orElseThrow { Exception("User does not exist") }


            val customer: Customer =
                customerRepository.findById(request.customer ?: throw Exception("Customer ID is required"))
                    .orElseThrow { Exception("Customer not found") }

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

            val savedInvoiceItems = invoiceItemRepository.saveAll(
                request.items.map {
                    InvoiceItem(
                        invoice = invoice,
                        createdAt = Instant.now(),
                        updatedAt = Instant.now(),
                        product = productRepository.getReferenceById(it.id),
                        description = it.description,
                        quantity = it.quantity,
                        unitPrice = it.unitPrice,
                    )
                }
            )

            return toInvoiceDto(invoice)

        } catch (e: Exception) {
            throw e
        }

    }

    @Cacheable(value = [CacheConfig.INVOICES], key = "#userId")
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

    @Cacheable(value = [CacheConfig.INVOICE], key = "#id")
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