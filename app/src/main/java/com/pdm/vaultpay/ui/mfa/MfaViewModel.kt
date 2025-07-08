package com.pdm.vaultpay.ui.mfa

import com.pdm.vaultpay.data.repository.AuthRepository
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MfaViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val code = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    val mfaSuccess = mutableStateOf(false)

    fun verifyMfa() {
        if (code.value.isBlank()) {
            errorMessage.value = "Code cannot be empty"
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            try {
                val result = authRepository.verifyMfa(code.value)
                if (result.success) {
                    mfaSuccess.value = true
                } else {
                    errorMessage.value = result.message ?: "Invalid code"
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Unknown error"
            } finally {
                isLoading.value = false
            }
        }
    }
}