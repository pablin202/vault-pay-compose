package com.pdm.vaultpay.data.remote

import com.pdm.vaultpay.data.remote.dto.*
import retrofit2.http.*

interface AuthApi {
    // Autenticación básica
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): SignupResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("auth/verify-email")
    suspend fun verifyEmail(@Query("token") token: String): EmailVerificationResponse

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): ForgotPasswordResponse

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): ResetPasswordResponse

    @GET("auth/profile")
    suspend fun getAuthProfile(): UserSafeData

    @POST("auth/logout")
    suspend fun logout(): LogoutResponse

    @POST("auth/mfa/setup")
    suspend fun setupMfa(): MfaSetupResponse

    @POST("auth/mfa/enable")
    suspend fun enableMfa(@Body request: MfaEnableRequest): MfaEnableResponse

    @POST("auth/mfa/disable")
    suspend fun disableMfa(@Body request: MfaDisableRequest): MfaDisableResponse

    @POST("auth/mfa/verify")
    suspend fun verifyMfa(@Body request: MfaRequest): MfaResponse
}