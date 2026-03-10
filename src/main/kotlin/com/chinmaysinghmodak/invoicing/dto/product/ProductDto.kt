package com.chinmaysinghmodak.invoicing.dto.product

import com.chinmaysinghmodak.invoicing.model.Product

data class ProductDto(
    var id: Long? = null,
    var name: String = "",
    var description: String? = null,
    var price: Double = 0.0,
    var stock: Int = 0,
)

fun toProductDto(product: Product): ProductDto {
    return ProductDto(
        id = product.id,
        name = product.name,
        description = product.description,
        price = product.unitPrice.toDouble(),
        stock = product.stock,
    )
}

