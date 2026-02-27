package com.chinmaysinghmodak.invoicing.dto

import com.chinmaysinghmodak.invoicing.model.OrgUser
import com.chinmaysinghmodak.invoicing.model.Organization

data class OrgRolesDto (
    var id: Long = 0,
    var name: String? = null,
    var roleName: String? = null,
)

fun toOrgRoleDto(orgUser: OrgUser): OrgRolesDto {
    return OrgRolesDto(
        id = orgUser.id,
        name = orgUser.organization?.name,
        roleName = orgUser.role?.name
    )
}