package com.chinmaysinghmodak.invoicing.dto

import com.chinmaysinghmodak.invoicing.model.OrgUser

data class OrgUserDto(
    var id: Long = 0,
    var user: UserDto? = null,
    var organizationId: Long? = null,
    var role: String? = null,
    var accessToken: String? = null,
    var refreshToken: String? = null,
)

fun orgUserToDto(orgUser: OrgUser, accessToken: String?, refreshToken: String?): OrgUserDto {
    return OrgUserDto(
        id = orgUser.id,
        user = userToDto(orgUser.user),
        organizationId = orgUser.organization?.id,
        role = orgUser.role?.name,
        accessToken = accessToken,
        refreshToken = refreshToken
    )

}