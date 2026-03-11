package com.chinmaysinghmodak.invoicing.service

import com.chinmaysinghmodak.invoicing.dto.product.CreateProductRequest
import com.chinmaysinghmodak.invoicing.dto.product.ProductDto
import com.chinmaysinghmodak.invoicing.dto.product.toProductDto
import com.chinmaysinghmodak.invoicing.model.OrgUser
import com.chinmaysinghmodak.invoicing.model.Product
import com.chinmaysinghmodak.invoicing.repository.OrgUserRepository
import com.chinmaysinghmodak.invoicing.repository.ProductRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ProductService(
    var productRepository: ProductRepository,
    var orgUserRepository: OrgUserRepository,
) {
    fun getAllProducts(userId: Long): List<ProductDto> {
        try {

            val user: OrgUser = orgUserRepository.findById(userId).orElseThrow { Exception("User not found") }

            val products: List<Product> = productRepository.findByOrganization(user.organization!!)

            return products.map { toProductDto(it) }
        } catch (e: Exception) {
            throw e
        }
    }

    @Transactional
    fun createProduct(userId: Long, request: CreateProductRequest): ProductDto {
        try {

            val user: OrgUser = orgUserRepository.findById(userId).orElseThrow { Exception("User not found") }


            val p = Product(
                name = request.name,
                description = request.description,
                unitPrice = request.price.toBigDecimal(),
                stock = request.stock,
                createdAt = Instant.now(),
                updatedAt = Instant.now(), organization = user.organization,

                )

            val product =  productRepository.save(p)

            return toProductDto(product)


        } catch (e: Exception) {
            throw e
        }
    }

    fun getProductById(userId: Long, productId: Long): ProductDto {
        try {

            val user: OrgUser = orgUserRepository.findById(userId).orElseThrow { Exception("User not found") }

            val product: Product = productRepository.findById(productId).orElseThrow { Exception("Product not found") }

            if (product.organization?.id != user.organization?.id) {
                throw Exception("Unauthorized access to product")
            }

            return toProductDto(product);
        } catch (e: Exception) {
            throw e
        }
    }
}