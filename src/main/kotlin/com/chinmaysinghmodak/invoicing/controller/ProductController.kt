package com.chinmaysinghmodak.invoicing.controller

import com.chinmaysinghmodak.invoicing.dto.common.ApiResponse
import com.chinmaysinghmodak.invoicing.dto.auth.JwtAuthenticationToken
import com.chinmaysinghmodak.invoicing.dto.product.CreateProductRequest
import com.chinmaysinghmodak.invoicing.dto.product.ProductDto
import com.chinmaysinghmodak.invoicing.model.Product
import com.chinmaysinghmodak.invoicing.service.ProductService
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
@RequestMapping("/products")
@Tag(name = "Product", description = "Product creation and management")
class ProductController(
    var productService: ProductService
) {

    @Operation(summary = "Create product", description = "Creates a new product under the authenticated user's organization")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/create")
    fun createProduct(@RequestBody @Valid request: CreateProductRequest): ResponseEntity<ApiResponse<ProductDto>> {

        try {

            val token = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken

            val product = productService.createProduct(token.userId, request);



            return ResponseEntity.ok().body(
                ApiResponse(
                    success = true, data = product, message = "Product Created Successfully"
                )
            )

        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(
                ApiResponse(
                    success = false,
                    message = "Failed to create product", error = e.message,
                )
            )
        }
    }

    @Operation(summary = "Update product", description = "Updates an existing product's details")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{id}")
    fun updateProduct(@PathVariable id: Long, @RequestBody product: Product) {

    }


    @Operation(summary = "Get product by ID", description = "Returns a single product by its ID within the authenticated user's organization")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    fun getProductById(@PathVariable("id") id: Long) : ResponseEntity<ApiResponse<ProductDto>> {
            try {

                val token = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken

                val product = productService.getProductById(token.userId, id)

                return ResponseEntity.ok().body(
                    ApiResponse(
                        success = true, data = product, message = "Product Fetched Successfully"
                    )
                )

            } catch (e: Exception) {
                return ResponseEntity.badRequest().body(
                    ApiResponse(
                        success = false,
                        message = "Failed to fetch product", error = e.message,
                    )
                )
            }
    }

    @Operation(summary = "Get all products", description = "Returns all products belonging to the authenticated user's organization")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/")
    fun getAllProducts(): ResponseEntity<ApiResponse<List<ProductDto>>> {

        try {

            val token = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken

            val products = productService.getAllProducts(token.userId)

            return ResponseEntity.ok().body(
                ApiResponse(
                    success = true, data = products, message = "Products Fetched Successfully"
                )
            )

        } catch (e: Exception) {

            return ResponseEntity.badRequest().body(
                ApiResponse(
                    message = "Products Fetch Failed",
                    error = e.message,
                    success = false,
                )
            )
        }

    }


}