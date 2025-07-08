package com.pdm.vaultpay.data.remote

import com.pdm.vaultpay.data.remote.dto.UpdateProfileRequest
import com.pdm.vaultpay.data.remote.dto.UpdateProfileResponse
import com.pdm.vaultpay.data.remote.dto.UserSafeData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserApi {
    @GET("users/profile")
    suspend fun getProfile(): UserSafeData

    @PUT("users/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): UpdateProfileResponse
}