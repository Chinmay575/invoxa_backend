package com.chinmaysinghmodak.invoicing.controller

import com.chinmaysinghmodak.invoicing.dto.common.ApiResponse
import com.chinmaysinghmodak.invoicing.dto.auth.JwtAuthenticationToken
import com.chinmaysinghmodak.invoicing.dto.product.ProductDto
import com.chinmaysinghmodak.invoicing.model.Product
import com.chinmaysinghmodak.invoicing.service.ProductService
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
class ProductController(
    var productService: ProductService
) {

    @PostMapping("/create")
    fun createProduct() {

    }

    @PatchMapping("/{id}")
    fun updateProduct(@PathVariable id: Long, @RequestBody product: Product) {

    }


    @GetMapping("/{id}")
    fun getProductById(@PathVariable("id") id: Long) {

    }

    @GetMapping("/")
    fun getAllProducts(): ResponseEntity<ApiResponse<List<ProductDto>>> {

        try {

            val token = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken

            val products = productService.getAllProducts(token.userId)

            return ResponseEntity.ok().body(
                ApiResponse(
                    success = true,
                    data = products,
                    message = "Products Fetched Successfully"
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