package com.pdm.vaultpay.core

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import kotlinx.coroutines.flow.first
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey

import javax.crypto.spec.GCMParameterSpec
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import javax.crypto.KeyGenerator
import com.pdm.vaultpay.utils.Constants

object SimpleCryptoHelper {
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val KEY_ALIAS = Constants.KEYSTORE_ALIAS
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val GCM_TAG_LENGTH = 128

    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

    private fun getSecretKey(): SecretKey {
        val existingKey = (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)
        if (existingKey != null) return existingKey

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }

    fun encrypt(data: String): Pair<ByteArray, ByteArray> {
        if (data.isBlank()) throw IllegalArgumentException("Data to encrypt cannot be blank")
        
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val key = getSecretKey()
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedBytes = cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))
        val iv = cipher.iv
        return encryptedBytes to iv
    }

    fun decrypt(encryptedBytes: ByteArray, iv: ByteArray): String {
        if (encryptedBytes.isEmpty()) throw IllegalArgumentException("Encrypted data cannot be empty")
        if (iv.isEmpty()) throw IllegalArgumentException("IV cannot be empty")
        
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val key = getSecretKey()
        val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, spec)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, StandardCharsets.UTF_8)
    }
}

// DataStore setup
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.DATASTORE_NAME)

object SecureTokenManager {
    private val TOKEN_KEY = stringPreferencesKey(Constants.TOKEN_KEY)
    private val IV_KEY = stringPreferencesKey(Constants.IV_KEY)

    suspend fun saveToken(context: Context, token: String) {
        if (token.isBlank()) throw IllegalArgumentException("Token cannot be blank")
        
        val (encryptedBytes, iv) = SimpleCryptoHelper.encrypt(token)
        val encryptedBase64 = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
        val ivBase64 = Base64.encodeToString(iv, Base64.NO_WRAP)

        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = encryptedBase64
            prefs[IV_KEY] = ivBase64
        }
    }

    suspend fun getToken(context: Context): String? {
        val prefs = context.dataStore.data.first()
        val encryptedBase64 = prefs[TOKEN_KEY]
        val ivBase64 = prefs[IV_KEY]

        if (encryptedBase64 == null || ivBase64 == null) return null

        val encryptedBytes = Base64.decode(encryptedBase64, Base64.NO_WRAP)
        val iv = Base64.decode(ivBase64, Base64.NO_WRAP)

        return runCatching {
            SimpleCryptoHelper.decrypt(encryptedBytes, iv)
        }.getOrElse { exception ->
            android.util.Log.e("SecureTokenManager", "Failed to decrypt token", exception)
            null
        }
    }

    suspend fun clearToken(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
            prefs.remove(IV_KEY)
        }
    }
}