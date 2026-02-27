package com.chinmaysinghmodak.invoicing.model

import java.io.Serializable

data class RolePermissionId(
    var role: Long = 0,
    var permission: Long = 0,
) : Serializable
