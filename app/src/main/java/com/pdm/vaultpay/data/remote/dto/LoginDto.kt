package com.pdm.vaultpay.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "access_token")
    val access_token: String,
    @Json(name = "user")
    val user: UserSafeData
)

@JsonClass(generateAdapter = true)
data class MfaRequiredResponse(
    @Json(name = "mfaRequired")
    val mfaRequired: Boolean,
    @Json(name = "message")
    val message: String
)

@JsonClass(generateAdapter = true)
data class UserSafeData(
    @Json(name = "id")
    val id: Int,
    @Json(name = "email")
    val email: String,
    @Json(name = "isEmailVerified")
    val isEmailVerified: Boolean,
    @Json(name = "isMfaEnabled")
    val isMfaEnabled: Boolean,
    @Json(name = "isActive")
    val isActive: Boolean? = null,
    @Json(name = "lastLoginAt")
    val lastLoginAt: String? = null,
    @Json(name = "createdAt")
    val createdAt: String? = null
)