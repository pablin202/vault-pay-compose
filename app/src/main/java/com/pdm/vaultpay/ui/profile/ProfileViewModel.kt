package com.pdm.vaultpay.ui.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.vaultpay.data.remote.dto.UserSafeData
import com.pdm.vaultpay.data.repository.AuthRepository
import com.pdm.vaultpay.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val userProfile = mutableStateOf<UserSafeData?>(null)
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    val successMessage = mutableStateOf<String?>(null)

    // Edit mode states
    val isEditMode = mutableStateOf(false)
    val firstName = mutableStateOf("")
    val lastName = mutableStateOf("")
    val phone = mutableStateOf("")

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            
            try {
                val profile = userRepository.getProfile()
                userProfile.value = profile
                // Initialize edit fields
                firstName.value = profile.email.substringBefore("@") // Fallback
                lastName.value = ""
                phone.value = ""
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Failed to load profile"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun enableEditMode() {
        isEditMode.value = true
        clearMessages()
    }

    fun cancelEdit() {
        isEditMode.value = false
        // Reset fields to original values
        val profile = userProfile.value
        firstName.value = profile?.email?.substringBefore("@") ?: ""
        lastName.value = ""
        phone.value = ""
        clearMessages()
    }

    fun saveProfile() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            successMessage.value = null
            
            try {
                val result = userRepository.updateProfile(
                    firstName = firstName.value.ifEmpty { null },
                    lastName = lastName.value.ifEmpty { null },
                    phone = phone.value.ifEmpty { null }
                )
                userProfile.value = result.user
                successMessage.value = result.message
                isEditMode.value = false
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Failed to update profile"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun clearMessages() {
        errorMessage.value = null
        successMessage.value = null
    }
}