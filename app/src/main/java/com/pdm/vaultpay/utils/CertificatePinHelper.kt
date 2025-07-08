package com.pdm.vaultpay.utils

import android.util.Log

/**
 * Helper instructions to get certificate pins from your server
 * Use this guide in development to get the actual certificate pins
 */
object CertificatePinHelper {
    
    /**
     * Instructions to get certificate pins manually:
     * 
     * Option 1 - Using OpenSSL command:
     * openssl s_client -servername bank-backend-production-8bb8.up.railway.app -connect bank-backend-production-8bb8.up.railway.app:443 | openssl x509 -pubkey -noout | openssl rsa -pubin -outform der | openssl dgst -sha256 -binary | openssl enc -base64
     * 
     * Option 2 - Using Browser:
     * 1. Open https://bank-backend-production-8bb8.up.railway.app in Chrome
     * 2. Click the lock icon → Certificate → Details
     * 3. Export the certificate
     * 4. Use online tools to calculate SHA-256 pin
     * 
     * Option 3 - Let OkHttp tell you the pins:
     * 1. Enable certificate pinning with a dummy pin in NetworkModule
     * 2. The error message will show the actual pins
     * 
     * Example error message:
     * "Certificate pinning failure!
     *  Peer certificate chain:
     *    sha256/XXXXXX: CN=*.railway.app
     *    sha256/YYYYYY: CN=Let's Encrypt Authority"
     * 
     * Use these SHA-256 values in network_security_config.xml
     */
    fun logInstructions() {
        Log.d("CertificatePin", """
            To get certificate pins for your server:
            
            1. Run this OpenSSL command:
            openssl s_client -servername bank-backend-production-8bb8.up.railway.app -connect bank-backend-production-8bb8.up.railway.app:443 | openssl x509 -pubkey -noout | openssl rsa -pubin -outform der | openssl dgst -sha256 -binary | openssl enc -base64
            
            2. Or temporarily add a dummy pin to OkHttp and let it fail to show real pins
            
            3. Update network_security_config.xml with the real pins
        """.trimIndent())
    }
}