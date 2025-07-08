package com.pdm.vaultpay.ui.forgot_password

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.vaultpay.data.repository.AuthRepository
import com.pdm.vaultpay.utils.Constants
import com.pdm.vaultpay.utils.ValidationUtils
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val email = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    val successMessage = mutableStateOf<String?>(null)
    val emailSent = mutableStateOf(false)
    val emailError = mutableStateOf<String?>(null)

    fun sendResetEmail() {
        // Validate email
        val emailValidation = ValidationUtils.validateEmail(email.value)
        if (!emailValidation.isValid) {
            emailError.value = emailValidation.error
            return
        }
        emailError.value = null

        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            successMessage.value = null
            
            try {
                val result = authRepository.forgotPassword(emailValidation.normalizedEmail)
                successMessage.value = result.message
                emailSent.value = true
            } catch (e: Exception) {
                errorMessage.value = e.message ?: Constants.ErrorMessages.NETWORK_ERROR
            } finally {
                isLoading.value = false
            }
        }
    }

    fun validateEmailRealTime(newEmail: String) {
        email.value = newEmail
        val validation = ValidationUtils.validateEmail(newEmail)
        emailError.value = if (newEmail.isNotEmpty() && !validation.isValid) validation.error else null
    }

    fun clearMessages() {
        errorMessage.value = null
        successMessage.value = null
    }
}