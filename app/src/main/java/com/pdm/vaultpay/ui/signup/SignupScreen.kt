package com.pdm.vaultpay.ui.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel


@Composable
fun SignupScreen(
    viewModel: SignupViewModel = koinViewModel(),
    onSignupSuccess: () -> Unit
) {
    val email by viewModel.email
    val password by viewModel.password
    val confirmPassword by viewModel.confirmPassword
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val signupSuccess by viewModel.signupSuccess

    if (signupSuccess) {
        onSignupSuccess()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { viewModel.email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { viewModel.password.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(8.dp))
        TextField(
            value = confirmPassword,
            onValueChange = { viewModel.confirmPassword.value = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(16.dp))
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.signup() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }
        }
        errorMessage?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = Color.Red)
        }
    }
}