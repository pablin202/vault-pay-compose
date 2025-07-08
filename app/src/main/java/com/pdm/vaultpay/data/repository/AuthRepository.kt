package com.pdm.vaultpay.data.repository

import com.pdm.vaultpay.data.remote.AuthApi
import com.pdm.vaultpay.data.remote.dto.*

class AuthRepository(
    private val api: AuthApi
) {
    suspend fun register(email: String, password: String): SignupResponse {
        return api.register(RegisterRequest(email, password))
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return api.login(LoginRequest(email, password))
    }

    suspend fun verifyEmail(token: String): EmailVerificationResponse {
        return api.verifyEmail(token)
    }

    suspend fun forgotPassword(email: String): ForgotPasswordResponse {
        return api.forgotPassword(ForgotPasswordRequest(email))
    }

    suspend fun resetPassword(token: String, password: String): ResetPasswordResponse {
        return api.resetPassword(ResetPasswordRequest(token, password))
    }

    suspend fun getAuthProfile(): UserSafeData {
        return api.getAuthProfile()
    }

    suspend fun logout(): LogoutResponse {
        return api.logout()
    }

    // MFA (Multi-Factor Authentication)
    suspend fun setupMfa(): MfaSetupResponse {
        return api.setupMfa()
    }

    suspend fun enableMfa(code: String): MfaEnableResponse {
        return api.enableMfa(MfaEnableRequest(code))
    }

    suspend fun disableMfa(password: String): MfaDisableResponse {
        return api.disableMfa(MfaDisableRequest(password))
    }

    suspend fun verifyMfa(code: String): MfaResponse {
        return api.verifyMfa(MfaRequest(code))
    }
}