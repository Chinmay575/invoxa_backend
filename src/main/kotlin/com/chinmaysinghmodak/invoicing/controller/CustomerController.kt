package com.chinmaysinghmodak.invoicing.controller

import com.chinmaysinghmodak.invoicing.dto.common.ApiResponse
import com.chinmaysinghmodak.invoicing.dto.customer.CreateCustomerRequest
import com.chinmaysinghmodak.invoicing.dto.customer.CustomerDto
import com.chinmaysinghmodak.invoicing.dto.auth.JwtAuthenticationToken
import com.chinmaysinghmodak.invoicing.dto.customer.UpdateCustomerRequest
import com.chinmaysinghmodak.invoicing.service.CustomerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customers")
@Tag(name = "Customer", description = "Customer creation and management")
class CustomerController(
    private val customerService: CustomerService
) {


    @Operation(
        summary = "Create customer",
        description = "Creates a new customer under the authenticated user's organization"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/create")
    fun createCustomer(@RequestBody @Valid customer: CreateCustomerRequest): ResponseEntity<ApiResponse<CustomerDto>> {

        try {
            val token = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken

            val customer = customerService.createCustomer(customer, token.userId)

            return ResponseEntity.ok(
                ApiResponse(
                    success = true,
                    data = customer,
                    message = "Customer created successfully"
                )
            )
        } catch (e: Exception) {
            return ResponseEntity.ok(
                ApiResponse(
                    success = false,
                    error = e.message,
                    message = "Customer creation failed"
                )
            )
        }
    }


    @Operation(
        summary = "Get all customers",
        description = "Returns all customers belonging to the authenticated user's organization"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/")
    fun getAllCustomers(): ResponseEntity<ApiResponse<List<CustomerDto>>> {

        try {
            val token = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken

            val customers = customerService.getAllCustomers(token.userId)

            return ResponseEntity.ok(
                ApiResponse(
                    success = true,
                    data = customers,
                    message = "Customers retrieved successfully"
                )
            )
        } catch (e: Exception) {
            return ResponseEntity.ok(
                ApiResponse(
                    success = false,
                    error = e.message,
                    message = "Failed to retrieve customers"
                )
            )
        }

    }

    @Operation(
        summary = "Get customer by ID",
        description = "Returns a single customer by their ID within the authenticated user's organization"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    fun getCustomerById(@PathVariable id: Long): ResponseEntity<ApiResponse<CustomerDto>> {

        try {
            val token = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken

            val customer = customerService.getCustomerById(id, token.userId)
            return ResponseEntity.ok(
                ApiResponse(
                    success = true,
                    data = customer,
                    message = "Customer retrieved successfully"
                )
            )
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(
                ApiResponse(
                    success = false,
                    error = e.message,
                    message = "Failed to retrieve customer with id: $id"
                )
            )
        }
    }

    @Operation(
        summary = "Update customer",
        description = "Updates an existing customer's details"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{id}")
    fun updateCustomer(
        @PathVariable id: Long,
        @RequestBody @Valid request: UpdateCustomerRequest
    ): ResponseEntity<ApiResponse<CustomerDto>> {

        try {
            val token = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken

            val customer = customerService.updateCustomer(id, request, token.userId)
            return ResponseEntity.ok(
                ApiResponse(
                    success = true,
                    data = customer,
                    message = "Customer updated successfully"
                )
            )
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(
                ApiResponse(
                    success = false,
                    error = e.message,
                    message = "Failed to update customer with id: $id"
                )
            )
        }

    }

}