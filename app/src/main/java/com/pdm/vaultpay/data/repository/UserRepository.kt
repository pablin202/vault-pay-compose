package com.pdm.vaultpay.data.repository

import com.pdm.vaultpay.data.remote.UserApi
import com.pdm.vaultpay.data.remote.dto.UpdateProfileRequest
import com.pdm.vaultpay.data.remote.dto.UpdateProfileResponse
import com.pdm.vaultpay.data.remote.dto.UserSafeData

class UserRepository(
    private val userApi: UserApi
) {
    suspend fun getProfile(): UserSafeData {
        return userApi.getProfile()
    }

    suspend fun updateProfile(
        firstName: String? = null,
        lastName: String? = null,
        phone: String? = null
    ): UpdateProfileResponse {
        val request = UpdateProfileRequest(
            firstName = firstName,
            lastName = lastName,
            phone = phone
        )
        return userApi.updateProfile(request)
    }
}