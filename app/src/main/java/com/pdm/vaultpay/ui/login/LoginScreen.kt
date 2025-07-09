package com.pdm.vaultpay.ui.login

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.toRange
import com.pdm.vaultpay.ui.components.*
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onMfaRequired: () -> Unit,
    onLoginSuccess: () -> Unit,
    onForgotPassword: () -> Unit = {}
) {
    val email by viewModel.email
    val password by viewModel.password
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val needsMfa by viewModel.needsMfa
    val loginSuccess by viewModel.loginSuccess

    // Animations
    var isVisible by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val backgroundOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "backgroundOffset"
    )

    LaunchedEffect(Unit) {
        isVisible = true
    }

    if (needsMfa) {
        onMfaRequired()
    }

    if (loginSuccess) {
        onLoginSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1a1a2e),
                        Color(0xFF16213e),
                        Color(0xFF0f3460)
                    ),
                    start = androidx.compose.ui.geometry.Offset(backgroundOffset * 1000, 0f),
                    end = androidx.compose.ui.geometry.Offset(1000f + backgroundOffset * 1000, 1000f)
                )
            )
    ) {
        // Floating particles effect
        FloatingParticles()

        ScrollableColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { -100 },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ) + fadeIn(animationSpec = tween(1000))
            ) {
                // Logo and Title Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Animated Logo
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF667eea),
                                        Color(0xFF764ba2)
                                    )
                                ),
                                shape = RoundedCornerShape(30.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = "VaultPay Logo",
                            tint = Color.White,
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Welcome Back",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Sign in to continue to VaultPay",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Login Form
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { 200 },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                    )
                ) + fadeIn(animationSpec = tween(1000, delayMillis = 300))
            ) {
                GlassmorphismCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ModernTextField(
                            value = email,
                            onValueChange = { viewModel.email.value = it },
                            label = "Email Address",
                            leadingIcon = Icons.Default.Email,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        ModernTextField(
                            value = password,
                            onValueChange = { viewModel.password.value = it },
                            label = "Password",
                            leadingIcon = Icons.Default.Lock,
                            isPassword = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        GradientButton(
                            text = "Sign In",
                            onClick = { viewModel.login() },
                            isLoading = isLoading,
                            enabled = email.isNotEmpty() && password.isNotEmpty() && !isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        TextButton(
                            onClick = onForgotPassword
                        ) {
                            Text(
                                text = "Forgot Password?",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Error Message
            errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(24.dp))
                
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    if (error.contains("401") || error.lowercase().contains("verify") || error.lowercase().contains("email")) {
                        StatusCard(
                            title = "Email Verification Required",
                            description = "Please verify your email address before logging in. Check your inbox for a verification link.",
                            icon = Icons.Default.MarkEmailUnread,
                            isSuccess = false,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        StatusCard(
                            title = "Login Failed",
                            description = error,
                            icon = Icons.Default.Error,
                            isSuccess = false,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Additional Info
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 800))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Secured with 256-bit encryption",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun FloatingParticles() {
    val particles = remember { 
        (1..15).map { 
            Particle(
                x = (0..100).random(),
                y = (0..100).random(),
                size = (4..12).random(),
                speed = Random.nextFloat() * 1.2f + 0.3f,
            )
        }
    }
    
    particles.forEach { particle ->
        val infiniteTransition = rememberInfiniteTransition(label = "particle_${particle.hashCode()}")
        val yOffset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 100f,
            animationSpec = infiniteRepeatable(
                animation = tween((10000 / particle.speed).toInt(), easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "yOffset"
        )
        
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.1f,
            targetValue = 0.4f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000),
                repeatMode = RepeatMode.Reverse
            ),
            label = "alpha"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopStart)
                .offset(
                    x = (particle.x * 4).dp,
                    y = (yOffset * 8).dp
                )
        ) {
            Box(
                modifier = Modifier
                    .size(particle.size.dp)
                    .background(
                        Color.White.copy(alpha = alpha),
                        shape = RoundedCornerShape(50)
                    )
            )
        }
    }
}

private data class Particle(
    val x: Int,
    val y: Int,
    val size: Int,
    val speed: Float
)

@Composable
private fun ScrollableColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = modifier.verticalScroll(scrollState),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}