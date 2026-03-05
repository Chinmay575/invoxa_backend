package com.chinmaysinghmodak.invoicing.service

import com.chinmaysinghmodak.invoicing.dto.ProductDto
import com.chinmaysinghmodak.invoicing.dto.toProductDto
import com.chinmaysinghmodak.invoicing.model.OrgUser
import com.chinmaysinghmodak.invoicing.model.Product
import com.chinmaysinghmodak.invoicing.repository.OrgUserRepository
import com.chinmaysinghmodak.invoicing.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(
    var productRepository: ProductRepository,
    var orgUserRepository: OrgUserRepository,
) {
    fun getAllProducts(userId: Long): List<ProductDto> {
        try {

            var user: OrgUser = orgUserRepository.findById(userId).orElseThrow { Exception("User not found") }

            val products: List<Product> = productRepository.findByOrganization(user.organization!!)

            return products.map { toProductDto(it) }
        } catch (e: Exception) {
            throw e
        }
    }
}