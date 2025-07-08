package com.pdm.vaultpay.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ForgotPasswordRequest(
    @Json(name = "email")
    val email: String
)

@JsonClass(generateAdapter = true)
data class ForgotPasswordResponse(
    @Json(name = "message")
    val message: String
)

@JsonClass(generateAdapter = true)
data class ResetPasswordRequest(
    @Json(name = "token")
    val token: String,
    @Json(name = "password")
    val password: String
)

@JsonClass(generateAdapter = true)
data class ResetPasswordResponse(
    @Json(name = "message")
    val message: String
)