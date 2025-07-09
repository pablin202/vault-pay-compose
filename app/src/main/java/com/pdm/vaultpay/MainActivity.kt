package com.pdm.vaultpay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pdm.vaultpay.core.SecureTokenManager
import com.pdm.vaultpay.ui.dashboard.DashboardScreen
import com.pdm.vaultpay.ui.email_verification.EmailVerificationScreen
import com.pdm.vaultpay.ui.forgot_password.ForgotPasswordScreen
import com.pdm.vaultpay.ui.login.LoginScreen
import com.pdm.vaultpay.ui.mfa.MFAScreen
import com.pdm.vaultpay.ui.signup.SignupScreen
import com.pdm.vaultpay.ui.theme.VaultPayTheme
import com.pdm.vaultpay.ui.components.*
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
                SignupScreen(onSignupSuccess = { email ->
                    navController.navigate("email_verification/$email")
                })
            }

            composable("email_verification/{email}") { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email")
                EmailVerificationScreen(
                    userEmail = email,
                    onVerificationComplete = { 
                        navController.navigate("login") {
                            popUpTo("welcome") { inclusive = false }
                        }
                    },
                    onBackToLogin = { 
                        navController.navigate("login") {
                            popUpTo("welcome") { inclusive = false }
                        }
                    }
                )
            }

            composable("forgot_password") {
                ForgotPasswordScreen(
                    onBackToLogin = { navController.popBackStack() }
                )
            }

            composable("login") {
                LoginScreen(
                    onMfaRequired = { navController.navigate("mfa") },
                    onLoginSuccess = { navController.navigate("dashboard") },
                    onForgotPassword = { navController.navigate("forgot_password") }
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
    var isVisible by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "welcome_bg")
    val backgroundOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "backgroundOffset"
    )

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0f0c29),
                        Color(0xFF302b63),
                        Color(0xFF24243e)
                    ),
                    start = androidx.compose.ui.geometry.Offset(backgroundOffset * 500, 0f),
                    end = androidx.compose.ui.geometry.Offset(500f + backgroundOffset * 500, 1000f)
                )
            )
    ) {
        // Floating background elements
        WelcomeBackgroundElements()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { -200 },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ) + fadeIn(animationSpec = tween(1200))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Main Logo with glow effect
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF667eea),
                                        Color(0xFF764ba2),
                                        Color.Transparent
                                    ),
                                    radius = 300f
                                ),
                                shape = RoundedCornerShape(40.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = "VaultPay",
                            tint = Color.White,
                            modifier = Modifier.size(80.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "VaultPay",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Secure • Fast • Reliable",
                        fontSize = 18.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { 300 },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                ) + fadeIn(animationSpec = tween(1000, delayMillis = 600))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GradientButton(
                        text = "Sign In",
                        onClick = onLoginClick,
                        modifier = Modifier.fillMaxWidth(),
                        gradient = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF667eea),
                                Color(0xFF764ba2)
                            )
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = onSignupClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        border = BorderStroke(2.dp, Color.White.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Create Account",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 1000))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FeatureItem(
                            icon = Icons.Default.Security,
                            text = "Secure"
                        )
                        FeatureItem(
                            icon = Icons.Default.Speed,
                            text = "Fast"
                        )
                        FeatureItem(
                            icon = Icons.Default.Verified,
                            text = "Trusted"
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Bank-level security with cutting-edge technology",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureItem(
    icon: ImageVector,
    text: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun WelcomeBackgroundElements() {
    val elements = remember { 
        (1..8).map { 
            BackgroundElement(
                x = Random.nextInt(10, 91),
                y = Random.nextInt(10, 91),
                size = Random.nextInt(20, 61),
                speed = Random.nextFloat() * 1.2f + 0.3f,
                alpha = Random.nextFloat() * 0.2f + 0.1f
            )
        }
    }
    
    elements.forEach { element ->
        val infiniteTransition = rememberInfiniteTransition(label = "bg_element_${element.hashCode()}")
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween((20000 / element.speed).toInt(), easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "rotation"
        )
        
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopStart)
                .offset(
                    x = (element.x * 4).dp,
                    y = (element.y * 8).dp
                )
        ) {
            Box(
                modifier = Modifier
                    .size(element.size.dp)
                    .scale(scale)
                    .rotate(rotation)
                    .background(
                        Color.White.copy(alpha = element.alpha),
                        shape = RoundedCornerShape(8.dp)
                    )
            )
        }
    }
}

private data class BackgroundElement(
    val x: Int,
    val y: Int,
    val size: Int,
    val speed: Float,
    val alpha: Float
)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VaultPayTheme {
        VaultPayAppEntryPoint()
    }
}