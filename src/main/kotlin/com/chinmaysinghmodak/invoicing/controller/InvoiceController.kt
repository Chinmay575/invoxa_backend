package com.chinmaysinghmodak.invoicing.controller

import com.chinmaysinghmodak.invoicing.dto.ApiResponse
import com.chinmaysinghmodak.invoicing.dto.CreateInvoiceRequest
import com.chinmaysinghmodak.invoicing.dto.InvoiceDto
import com.chinmaysinghmodak.invoicing.dto.JwtAuthenticationToken
import com.chinmaysinghmodak.invoicing.service.InvoiceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/invoice")
@Tag(name = "Invoice", description = "Invoice creation and management")
class InvoiceController(
    private val invoiceService: InvoiceService
) {


    @Operation(summary = "Create invoice", description = "Creates a new invoice for the authenticated user's organization")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/create")
    fun createInvoice(@Valid @RequestBody request: CreateInvoiceRequest): ResponseEntity<ApiResponse<InvoiceDto>> {

        try {
            val token = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken;

            val invoice = invoiceService.createInvoice(request, token.userId)

            return ResponseEntity.ok(
                ApiResponse(
                    success = true, data = invoice, message = "Invoice created successfully"
                )
            )
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(
                ApiResponse(
                    success = false, error = e.message, message = "Invoice creation failed"
                )
            )
        }
    }

    @Operation(summary = "Update invoice", description = "Updates an existing invoice's details")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{id}")
    fun updateInvoice(@PathVariable id: Long) {

    }

    @Operation(summary = "Get all invoices", description = "Returns all invoices belonging to the authenticated user's organization")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/")
    fun getAllInvoices(): ResponseEntity<ApiResponse<List<InvoiceDto>>> {


        try {

            val token = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken


            invoiceService.getAllInvoices(token.userId)

            return ResponseEntity.ok().body(
                ApiResponse(
                    success = true,
                    data = invoiceService.getAllInvoices(token.userId),
                    message = "Invoices retrieved successfully"
                )
            )
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(
                ApiResponse(
                    success = false, error = e.message, message = "Failed to retrieve invoices"
                )
            )
        }
    }


    @Operation(summary = "Get invoice by ID", description = "Returns a single invoice by its ID within the authenticated user's organization")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    fun getInvoiceById(@PathVariable id: Long): ResponseEntity<ApiResponse<InvoiceDto>> {
        try {
            var token = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken

            invoiceService.getInvoiceById(id, token.userId)

            return ResponseEntity.ok().body(
                ApiResponse(
                    success = true,
                    data = invoiceService.getInvoiceById(id, token.userId),
                    message = "Invoice retrieved successfully"
                )
            )
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(
                ApiResponse(
                    success = false, error = e.message, message = "Failed to retrieve invoice"
                )
            )
        }
    }

    @Operation(summary = "Delete invoice", description = "Deletes an invoice by its ID")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    fun deleteInvoice(@PathVariable id: Long) {

    }
}