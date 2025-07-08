package com.pdm.vaultpay.utils

import android.util.Patterns
import java.util.regex.Pattern

object ValidationUtils {
    
    /**
     * Validación de password según especificación:
     * - Mínimo 8 caracteres
     * - Al menos una letra mayúscula
     * - Al menos una letra minúscula  
     * - Al menos un número
     * - Al menos un carácter especial (@$!%*?&)
     */
    fun validatePassword(password: String): PasswordValidationResult {
        val errors = mutableListOf<String>()
        
        if (password.length < Constants.PASSWORD_MIN_LENGTH) {
            errors.add("Password must be at least ${Constants.PASSWORD_MIN_LENGTH} characters long")
        }
        
        if (!password.any { it.isUpperCase() }) {
            errors.add("Password must contain at least one uppercase letter")
        }
        
        if (!password.any { it.isLowerCase() }) {
            errors.add("Password must contain at least one lowercase letter")
        }
        
        if (!password.any { it.isDigit() }) {
            errors.add("Password must contain at least one number")
        }
        
        if (!password.any { it in Constants.PASSWORD_SPECIAL_CHARS }) {
            errors.add("Password must contain at least one special character (${Constants.PASSWORD_SPECIAL_CHARS})")
        }
        
        return PasswordValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }
    
    /**
     * Validación de email
     */
    fun validateEmail(email: String): EmailValidationResult {
        val normalizedEmail = email.lowercase().trim()
        val isValid = Patterns.EMAIL_ADDRESS.matcher(normalizedEmail).matches()
        
        return EmailValidationResult(
            isValid = isValid,
            normalizedEmail = normalizedEmail,
            error = if (!isValid) "Please enter a valid email address" else null
        )
    }
    
    /**
     * Validación de código MFA
     */
    fun validateMfaCode(code: String): MfaCodeValidationResult {
        val cleanCode = code.trim()
        val isValid = cleanCode.length == Constants.MFA_CODE_LENGTH && cleanCode.all { it.isDigit() }
        
        return MfaCodeValidationResult(
            isValid = isValid,
            cleanCode = cleanCode,
            error = if (!isValid) "MFA code must be exactly ${Constants.MFA_CODE_LENGTH} digits" else null
        )
    }
    
    /**
     * Validación de confirmación de password
     */
    fun validatePasswordConfirmation(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}

data class PasswordValidationResult(
    val isValid: Boolean,
    val errors: List<String>
) {
    val errorMessage: String?
        get() = if (errors.isNotEmpty()) errors.joinToString("\n") else null
}

data class EmailValidationResult(
    val isValid: Boolean,
    val normalizedEmail: String,
    val error: String?
)

data class MfaCodeValidationResult(
    val isValid: Boolean,
    val cleanCode: String,
    val error: String?
)