package com.chinmaysinghmodak.invoicing.dto

import com.chinmaysinghmodak.invoicing.model.User

data class UserDto(
    var id: Long = 0,
    var username: String? = null,
    var fullName: String? = null,
    var email: String? = null,
    var isEmailVerified: Boolean = false,
    var profilePic: String? = null,
    var mobile: String? = null,
)

fun userToDto(user: User?): UserDto? {
    if(user == null) return null
    return UserDto(
        id = user.id,
        username = user.username,
        fullName = user.fullName,
        email = user.email,
        isEmailVerified = user.isEmailVerified,
        profilePic = user.profilePic,
        mobile = user.mobile
    )
}