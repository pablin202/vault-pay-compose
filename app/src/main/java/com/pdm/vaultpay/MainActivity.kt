package com.pdm.vaultpay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pdm.vaultpay.core.SecureTokenManager
import com.pdm.vaultpay.ui.dashboard.DashboardScreen
import com.pdm.vaultpay.ui.login.LoginScreen
import com.pdm.vaultpay.ui.mfa.MFAScreen
import com.pdm.vaultpay.ui.signup.SignupScreen
import com.pdm.vaultpay.ui.theme.VaultPayTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VaultPayTheme {
                VaultPayAppEntryPoint()
            }
        }
    }
}

@Composable
fun VaultPayAppEntryPoint() {
    val context = LocalContext.current
    val navController = rememberNavController()
    var startDestination by remember { mutableStateOf("welcome") }
    
    LaunchedEffect(Unit) {
        val token = SecureTokenManager.getToken(context)
        startDestination = if (token != null) {
            "dashboard"
        } else {
            "welcome"
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("welcome") {
                WelcomeScreen(
                    onLoginClick = { navController.navigate("login") },
                    onSignupClick = { navController.navigate("signup") }
                )
            }

            composable("signup") {
                SignupScreen(onSignupSuccess = {
                    navController.navigate("login")
                })
            }

            composable("login") {
                LoginScreen(
                    onMfaRequired = { navController.navigate("mfa") },
                    onLoginSuccess = { navController.navigate("dashboard") }
                )
            }

            composable("mfa") {
                MFAScreen(
                    onMfaSuccess = { navController.navigate("dashboard") }
                )
            }

            composable("dashboard") {
                DashboardScreen(
                    onLogout = {
                        navController.navigate("welcome") {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to VaultPay!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Button (onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onSignupClick, modifier = Modifier.fillMaxWidth()) {
            Text("Signup")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VaultPayTheme {
        VaultPayAppEntryPoint()
    }
}