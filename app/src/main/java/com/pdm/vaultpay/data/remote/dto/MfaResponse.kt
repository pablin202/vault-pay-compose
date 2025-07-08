package com.pdm.vaultpay.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MfaResponse(
    @Json(name = "success")
    val success: Boolean,
    @Json(name = "message")
    val message: String? = null
)