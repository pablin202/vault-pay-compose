package com.pdm.vaultpay.ui.login

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.vaultpay.core.SecureTokenManager
import com.pdm.vaultpay.data.repository.AuthRepository
import com.pdm.vaultpay.utils.Constants
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val context: Context
) : ViewModel() {
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val needsMfa = mutableStateOf(false)
    val loginSuccess = mutableStateOf(false)
    val jwtToken = mutableStateOf<String?>(null)
    val errorMessage = mutableStateOf<String?>(null)

    fun login() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val result = authRepository.login(email.value, password.value)
                jwtToken.value = result.access_token
                
                // Check if user needs MFA based on user data
                if (result.user.isMfaEnabled) {
                    needsMfa.value = true
                } else {
                    // Store token securely if login is successful and no MFA required
                    SecureTokenManager.saveToken(context, result.access_token)
                    loginSuccess.value = true
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: Constants.ErrorMessages.UNKNOWN_ERROR
            } finally {
                isLoading.value = false
            }
        }
    }
    
    fun completeMfaLogin() {
        viewModelScope.launch {
            if (jwtToken.value != null) {
                SecureTokenManager.saveToken(context, jwtToken.value!!)
                loginSuccess.value = true
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            SecureTokenManager.clearToken(context)
            jwtToken.value = null
            loginSuccess.value = false
            needsMfa.value = false
            errorMessage.value = null
        }
    }
}