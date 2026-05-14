package org.nextinfo.project.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ForgotPasswordScreen(onBack: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var isLoading  by remember { mutableStateOf(false) }
    var isSent by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    AuthScaffold {
        // Back button
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = AuthTextPrimary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Icon
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(AuthBluePale),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                tint = AuthBlue,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedContent(targetState = isSent, label = "forgotState") { sent ->
            if (!sent) {
                Column {
                    Text(
                        text = "Forgot Password?",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AuthTextPrimary
                    )
                    Text(
                        text = "No worries! Enter your email and we'll send you a reset link.",
                        fontSize = 14.sp,
                        color = AuthTextSecond,
                        modifier = Modifier.padding(top = 6.dp, bottom = 28.dp),
                        lineHeight = 20.sp
                    )

                    AnimatedVisibility(visible = errorMsg != null) {
                        ErrorBanner(message = errorMsg ?: "") { errorMsg = null }
                    }

                    AuthTextField(
                        value = email,
                        onValueChange = { email = it; errorMsg = null },
                        label = "Email Address",
                        leadingIcon = Icons.Outlined.Email,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    AuthPrimaryButton(
                        text = "Send Reset Link",
                        isLoading = isLoading,
                        onClick = {
                            when {
                                email.isBlank() -> errorMsg = "Please enter your email"
                                !email.contains("@") -> errorMsg = "Please enter a valid email"
                                else -> { isLoading = true; isSent = true }
                            }
                        }
                    )
                }
            } else {
                // Success state
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFECFDF5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Check your email",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AuthTextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "We sent a password reset link to\n$email",
                        fontSize = 14.sp,
                        color = AuthTextSecond,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    AuthPrimaryButton(
                        text = "Back to Sign In",
                        onClick = onBack
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = { isSent = false; isLoading = false }) {
                        Text(
                            text = "Resend email",
                            fontSize = 13.sp,
                            color = AuthBlue,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}