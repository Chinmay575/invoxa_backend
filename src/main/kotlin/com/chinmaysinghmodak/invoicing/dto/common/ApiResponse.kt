package com.chinmaysinghmodak.invoicing.dto.common

data class ApiResponse<T>(
    var success: Boolean = false, var message: String = "", var data: T? = null, var error: Any? = null
)

