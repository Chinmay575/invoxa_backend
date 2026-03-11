package com.chinmaysinghmodak.invoicing.dto.product

data class CreateProductRequest (
    val name : String ,
    val description : String? = null ,
    val price : Double ,
    val stock : Int
)