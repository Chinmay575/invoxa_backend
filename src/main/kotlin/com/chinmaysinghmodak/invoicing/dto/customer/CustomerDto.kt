package com.chinmaysinghmodak.invoicing.dto.customer

import com.chinmaysinghmodak.invoicing.model.Customer

data class CustomerDto(
    var id: Long = 0,
    var name: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var address: String? = null,
    var taxNumber: String? = null,
)

fun toCustomerDto(customer: Customer): CustomerDto {
    return CustomerDto(
        id = customer.id,
        name = customer.name,
        email = customer.email,
        phone = customer.mobile,
        address = customer.address,
        taxNumber = customer.taxNumber
    )
}

