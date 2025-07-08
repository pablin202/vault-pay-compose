package com.pdm.vaultpay.network

import android.content.Context
import com.pdm.vaultpay.core.SecureTokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        
        // Get token synchronously for the interceptor
        val token = runBlocking {
            SecureTokenManager.getToken(context)
        }
        
        // Add Authorization header if token exists
        val newRequest = if (token != null) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }
        
        return chain.proceed(newRequest)
    }
}