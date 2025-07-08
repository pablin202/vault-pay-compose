package com.pdm.vaultpay.ui.email_verification

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.vaultpay.data.repository.AuthRepository
import com.pdm.vaultpay.utils.Constants
import kotlinx.coroutines.launch

class EmailVerificationViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    val successMessage = mutableStateOf<String?>(null)
    val isVerified = mutableStateOf(false)
    val userEmail = mutableStateOf<String?>(null)

    fun verifyEmailWithToken(token: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            successMessage.value = null
            
            try {
                val result = authRepository.verifyEmail(token)
                successMessage.value = result.message
                isVerified.value = result.verified
            } catch (e: Exception) {
                errorMessage.value = e.message ?: Constants.ErrorMessages.NETWORK_ERROR
            } finally {
                isLoading.value = false
            }
        }
    }

    fun resendVerificationEmail(email: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            successMessage.value = null
            
            try {
                // Use forgot password endpoint to resend verification
                val result = authRepository.forgotPassword(email)
                successMessage.value = "Verification email sent to $email"
            } catch (e: Exception) {
                errorMessage.value = e.message ?: Constants.ErrorMessages.NETWORK_ERROR
            } finally {
                isLoading.value = false
            }
        }
    }

    fun setUserEmail(email: String) {
        userEmail.value = email
    }

    fun clearMessages() {
        errorMessage.value = null
        successMessage.value = null
    }
}