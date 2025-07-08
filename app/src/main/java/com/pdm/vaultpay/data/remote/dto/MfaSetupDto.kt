package com.pdm.vaultpay.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MfaSetupResponse(
    @Json(name = "secret")
    val secret: String,
    @Json(name = "qrCodeDataURL")
    val qrCodeDataURL: String,
    @Json(name = "backupCodes")
    val backupCodes: List<String>
)

@JsonClass(generateAdapter = true)
data class MfaEnableRequest(
    @Json(name = "code")
    val code: String
)

@JsonClass(generateAdapter = true)
data class MfaEnableResponse(
    @Json(name = "message")
    val message: String,
    @Json(name = "backupCodes")
    val backupCodes: List<String>
)

@JsonClass(generateAdapter = true)
data class MfaDisableRequest(
    @Json(name = "password")
    val password: String
)

@JsonClass(generateAdapter = true)
data class MfaDisableResponse(
    @Json(name = "message")
    val message: String
)