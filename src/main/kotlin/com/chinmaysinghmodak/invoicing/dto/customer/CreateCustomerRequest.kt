package com.chinmaysinghmodak.invoicing.dto.customer

data class CreateCustomerRequest (
    var name: String = "",
    var email: String? = null,
    var address: String? = null,
    var mobile: String? = null,
    var taxNumber: String? = null,
)

