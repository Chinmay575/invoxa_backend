package com.chinmaysinghmodak.invoicing.dto.auth

data class UpdateProfileRequest(
    var fullName: String? = null,
    var mobile: String? = null,
    var username: String? = null,
    var profilePic: String? = null,
)

