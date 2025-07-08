package com.pdm.vaultpay.ui.signup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.vaultpay.data.repository.AuthRepository
import com.pdm.vaultpay.utils.Constants
import com.pdm.vaultpay.utils.ValidationUtils
import kotlinx.coroutines.launch

class SignupViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    val signupSuccess = mutableStateOf(false)
    val passwordValidationErrors = mutableStateOf<List<String>>(emptyList())
    val emailError = mutableStateOf<String?>(null)

    fun signup() {
        // Validate email
        val emailValidation = ValidationUtils.validateEmail(email.value)
        if (!emailValidation.isValid) {
            emailError.value = emailValidation.error
            return
        }
        emailError.value = null

        // Validate password
        val passwordValidation = ValidationUtils.validatePassword(password.value)
        if (!passwordValidation.isValid) {
            passwordValidationErrors.value = passwordValidation.errors
            errorMessage.value = passwordValidation.errorMessage
            return
        }
        passwordValidationErrors.value = emptyList()

        // Validate password confirmation
        if (!ValidationUtils.validatePasswordConfirmation(password.value, confirmPassword.value)) {
            errorMessage.value = "Passwords do not match"
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val result = authRepository.register(emailValidation.normalizedEmail, password.value)
                signupSuccess.value = true
            } catch (e: Exception) {
                errorMessage.value = e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR
            } finally {
                isLoading.value = false
            }
        }
    }

    fun validatePasswordRealTime(newPassword: String) {
        password.value = newPassword
        val validation = ValidationUtils.validatePassword(newPassword)
        passwordValidationErrors.value = validation.errors
    }

    fun validateEmailRealTime(newEmail: String) {
        email.value = newEmail
        val validation = ValidationUtils.validateEmail(newEmail)
        emailError.value = if (newEmail.isNotEmpty() && !validation.isValid) validation.error else null
    }
}