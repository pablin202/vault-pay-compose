package com.pdm.vaultpay.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignupResponse(
    @Json(name = "message")
    val message: String,
    @Json(name = "userId")
    val userId: Int
)