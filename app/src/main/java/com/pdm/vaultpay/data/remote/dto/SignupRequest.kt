package com.pdm.vaultpay.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String
)

// Mantener SignupRequest para compatibilidad hacia atrás
@JsonClass(generateAdapter = true)
data class SignupRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String
)