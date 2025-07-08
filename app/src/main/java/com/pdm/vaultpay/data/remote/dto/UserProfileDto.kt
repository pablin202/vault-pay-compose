package com.pdm.vaultpay.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateProfileRequest(
    @Json(name = "firstName")
    val firstName: String? = null,
    @Json(name = "lastName")
    val lastName: String? = null,
    @Json(name = "phone")
    val phone: String? = null
)

@JsonClass(generateAdapter = true)
data class UpdateProfileResponse(
    @Json(name = "message")
    val message: String,
    @Json(name = "user")
    val user: UserSafeData
)

@JsonClass(generateAdapter = true)
data class LogoutResponse(
    @Json(name = "message")
    val message: String
)

@JsonClass(generateAdapter = true)
data class EmailVerificationResponse(
    @Json(name = "message")
    val message: String,
    @Json(name = "verified")
    val verified: Boolean
)