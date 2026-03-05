package com.chinmaysinghmodak.invoicing.repository

import com.chinmaysinghmodak.invoicing.model.Organization
import com.chinmaysinghmodak.invoicing.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findByOrganization(org: Organization) : List<Product>
}