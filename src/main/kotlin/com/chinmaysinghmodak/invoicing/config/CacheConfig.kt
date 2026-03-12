package com.chinmaysinghmodak.invoicing.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class CacheConfig {

    companion object {
        const val USER_PROFILE = "userProfile"
        const val PRODUCTS = "products"
        const val PRODUCT = "product"
        const val CUSTOMERS = "customers"
        const val CUSTOMER = "customer"
        const val INVOICES = "invoices"
        const val INVOICE = "invoice"
    }

    @Bean
    fun cacheManager(): CacheManager {
        val manager = CaffeineCacheManager(
            USER_PROFILE,
            PRODUCTS, PRODUCT,
            CUSTOMERS, CUSTOMER,
            INVOICES, INVOICE,
        )
        manager.setCaffeine(
            Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats()
        )
        return manager
    }
}

