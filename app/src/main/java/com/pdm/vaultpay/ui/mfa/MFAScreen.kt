package com.pdm.vaultpay.ui.mfa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pdm.vaultpay.ui.login.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MFAScreen(
    mfaViewModel: MfaViewModel = koinViewModel(),
    loginViewModel: LoginViewModel = koinViewModel(),
    onMfaSuccess: () -> Unit
) {
    val code by mfaViewModel.code
    val isLoading by mfaViewModel.isLoading
    val errorMessage by mfaViewModel.errorMessage
    val mfaSuccess by mfaViewModel.mfaSuccess

    LaunchedEffect(mfaSuccess) {
        if (mfaSuccess) {
            loginViewModel.completeMfaLogin()
            onMfaSuccess()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = code,
            onValueChange = { mfaViewModel.code.value = it },
            label = { Text("MFA Code") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { mfaViewModel.verifyMfa() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Verify")
        }
        errorMessage?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = Color.Red)
        }
    }
}