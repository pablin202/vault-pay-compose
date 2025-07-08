package com.pdm.vaultpay.utils

object Constants {
    // Network Configuration
    const val BASE_URL = "https://bank-backend-production-8bb8.up.railway.app/"
    const val API_VERSION = "v1"
    const val TIMEOUT_SECONDS = 30L
    const val CONNECT_TIMEOUT_SECONDS = 30L
    const val READ_TIMEOUT_SECONDS = 30L
    
    // Security Configuration
    const val KEYSTORE_ALIAS = "vaultpay_key_alias"
    const val DATASTORE_NAME = "settings"
    const val TOKEN_KEY = "jwt_token_encrypted"
    const val IV_KEY = "jwt_token_iv"
    
    // Rate Limiting (informational - handled by backend)
    const val LOGIN_MAX_ATTEMPTS = 5
    const val LOGIN_LOCKOUT_MINUTES = 15
    const val PASSWORD_RESET_MAX_ATTEMPTS = 3
    const val PASSWORD_RESET_LOCKOUT_HOURS = 1
    const val MFA_MAX_ATTEMPTS = 10
    const val MFA_LOCKOUT_MINUTES = 15
    
    // Account Security
    const val ACCOUNT_LOCKOUT_ATTEMPTS = 5
    const val ACCOUNT_LOCKOUT_HOURS = 2
    
    // Session Management
    const val SESSION_TIMEOUT_MINUTES = 30
    const val TOKEN_REFRESH_THRESHOLD_MINUTES = 5
    
    // Password Requirements
    const val PASSWORD_MIN_LENGTH = 8
    const val PASSWORD_SPECIAL_CHARS = "@\$!%*?&"
    
    // MFA
    const val MFA_CODE_LENGTH = 6
    
    // Debug Configuration
    const val DEBUG = true // Set to false for production
    
    // Error Messages
    object ErrorMessages {
        const val NETWORK_ERROR = "Network connection error. Please check your internet connection."
        const val TIMEOUT_ERROR = "Request timed out. Please try again."
        const val SERVER_ERROR = "Server error. Please try again later."
        const val UNAUTHORIZED_ERROR = "Session expired. Please log in again."
        const val VALIDATION_ERROR = "Please check your input and try again."
        const val UNKNOWN_ERROR = "An unexpected error occurred. Please try again."
    }
    
    // Success Messages
    object SuccessMessages {
        const val LOGIN_SUCCESS = "Welcome back!"
        const val REGISTRATION_SUCCESS = "Account created successfully!"
        const val PASSWORD_RESET_SENT = "Password reset link sent to your email."
        const val PASSWORD_RESET_SUCCESS = "Password reset successfully."
        const val PROFILE_UPDATED = "Profile updated successfully."
        const val MFA_ENABLED = "Two-factor authentication enabled."
        const val MFA_DISABLED = "Two-factor authentication disabled."
        const val LOGOUT_SUCCESS = "Logged out successfully."
    }
    
    // Validation Patterns
    object ValidationPatterns {
        const val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        const val PHONE_PATTERN = "^[+]?[1-9]\\d{1,14}\$"
    }
}