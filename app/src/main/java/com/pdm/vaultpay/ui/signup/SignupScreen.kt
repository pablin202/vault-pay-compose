package com.pdm.vaultpay.ui.signup

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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.vaultpay.ui.components.*
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random


@Composable
fun SignupScreen(
    viewModel: SignupViewModel = koinViewModel(),
    onSignupSuccess: (String) -> Unit
) {
    val email by viewModel.email
    val password by viewModel.password
    val confirmPassword by viewModel.confirmPassword
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val signupSuccess by viewModel.signupSuccess
    val emailError by viewModel.emailError
    val passwordValidationErrors by viewModel.passwordValidationErrors

    // Animations
    var isVisible by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "signup_background")
    val backgroundOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "backgroundOffset"
    )

    LaunchedEffect(Unit) {
        isVisible = true
    }

    if (signupSuccess) {
        onSignupSuccess(email)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF2c1810),
                        Color(0xFF3d2f1f),
                        Color(0xFF1a1a2e)
                    ),
                    start = androidx.compose.ui.geometry.Offset(backgroundOffset * 800, 0f),
                    end = androidx.compose.ui.geometry.Offset(800f + backgroundOffset * 800, 1200f)
                )
            )
    ) {
        // Floating background elements
        SignupBackgroundElements()

        ScrollableColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo and Title Section
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { -150 },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ) + fadeIn(animationSpec = tween(1200))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Modern Logo with glow effect
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFFf093fb),
                                        Color(0xFFf5576c),
                                        Color.Transparent
                                    ),
                                    radius = 200f
                                ),
                                shape = RoundedCornerShape(25.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Sign Up",
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Create Account",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Join VaultPay for secure banking",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Signup Form
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { 250 },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                    )
                ) + fadeIn(animationSpec = tween(1200, delayMillis = 400))
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
                            onValueChange = { viewModel.validateEmailRealTime(it) },
                            label = "Email Address",
                            leadingIcon = Icons.Default.Email,
                            isError = emailError != null,
                            errorMessage = emailError,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        ModernTextField(
                            value = password,
                            onValueChange = { viewModel.validatePasswordRealTime(it) },
                            label = "Password",
                            leadingIcon = Icons.Default.Lock,
                            isPassword = true,
                            isError = passwordValidationErrors.isNotEmpty(),
                            errorMessage = passwordValidationErrors.take(2).joinToString("\n"),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        ModernTextField(
                            value = confirmPassword,
                            onValueChange = { viewModel.confirmPassword.value = it },
                            label = "Confirm Password",
                            leadingIcon = Icons.Default.Lock,
                            isPassword = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        GradientButton(
                            text = "Create Account",
                            onClick = { viewModel.signup() },
                            isLoading = isLoading,
                            enabled = email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && !isLoading,
                            modifier = Modifier.fillMaxWidth(),
                            gradient = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFf093fb),
                                    Color(0xFFf5576c)
                                )
                            )
                        )
                    }
                }
            }

            // Password Requirements
            if (passwordValidationErrors.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    StatusCard(
                        title = "Password Requirements",
                        description = passwordValidationErrors.joinToString("\n"),
                        icon = Icons.Default.Info,
                        isSuccess = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Error Message
            errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    StatusCard(
                        title = "Signup Failed",
                        description = error,
                        icon = Icons.Default.Error,
                        isSuccess = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Security Info
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 1000))
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    SecurityFeature(
                        icon = Icons.Default.Shield,
                        text = "Encrypted"
                    )
                    SecurityFeature(
                        icon = Icons.Default.VerifiedUser,
                        text = "Verified"
                    )
                    SecurityFeature(
                        icon = Icons.Default.Security,
                        text = "Protected"
                    )
                }
            }
        }
    }
}

@Composable
private fun SecurityFeature(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.size(20.dp)
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
private fun SignupBackgroundElements() {
    val elements = remember { 
        (1..6).map { 
            BackgroundElement(
                x = (5..95).random(),
                y = (5..95).random(),
                size = (30..80).random(),
                speed = Random.nextFloat() * 1.2f + 0.3f,
                alpha = Random.nextFloat() * 0.2f + 0.1f
            )
        }
    }
    
    elements.forEach { element ->
        val infiniteTransition = rememberInfiniteTransition(label = "signup_element_${element.hashCode()}")
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween((25000 / element.speed).toInt(), easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "rotation"
        )
        
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.7f,
            targetValue = 1.3f,
            animationSpec = infiniteRepeatable(
                animation = tween(5000),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopStart)
                .offset(
                    x = (element.x * 3.5f).dp,
                    y = (element.y * 7).dp
                )
        ) {
            Box(
                modifier = Modifier
                    .size(element.size.dp)
                    .scale(scale)
                    .rotate(rotation)
                    .background(
                        Color.White.copy(alpha = element.alpha),
                        shape = RoundedCornerShape(12.dp)
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