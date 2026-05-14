package org.nextinfo.project.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class AuthScreen {
    data object Login : AuthScreen()
    data object Register : AuthScreen()
    data object ForgotPassword : AuthScreen()
}

@Composable
fun AuthHost(onAuthSuccess: () -> Unit) {
    var currentScreen by remember { mutableStateOf<AuthScreen>(AuthScreen.Login) }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            fadeIn(tween(300)) + slideInHorizontally(tween(300)) { it / 6 } togetherWith
                    fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { -it / 6 }
        },
        label = "authTransition"
    ) { screen ->
        when (screen) {
            AuthScreen.Login -> LoginScreen(
                onLoginSuccess  = onAuthSuccess,
                onRegister = { currentScreen = AuthScreen.Register },
                onForgotPass = { currentScreen = AuthScreen.ForgotPassword }
            )
            AuthScreen.Register -> RegisterScreen(
                onRegisterSuccess = onAuthSuccess,
                onLogin = { currentScreen = AuthScreen.Login }
            )
            AuthScreen.ForgotPassword -> ForgotPasswordScreen(
                onBack = { currentScreen = AuthScreen.Login }
            )
        }
    }
}


/** App logo + name */
@Composable
fun ColumnScope.AuthBrand() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.linearGradient(listOf(AuthBlue, AuthBlueLight))
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = "NextInfo",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AuthBlue
        )
    }
}

/** Reusable text field */
@Composable
fun AuthTextField(
    value : String,
    onValueChange : (String) -> Unit,
    label : String,
    leadingIcon : ImageVector,
    isPassword : Boolean = false,
    passwordVisible : Boolean = false,
    onTogglePass : () -> Unit = {},
    keyboardOptions : KeyboardOptions = KeyboardOptions.Default,
    keyboardActions : KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 13.sp) },
        leadingIcon = {
            Icon(leadingIcon, contentDescription = null, tint = AuthBlue, modifier = Modifier.size(20.dp))
        },
        trailingIcon  = if (isPassword) ({
            IconButton(onClick = onTogglePass) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                    contentDescription = if (passwordVisible) "Hide" else "Show",
                    tint = AuthTextSecond,
                    modifier = Modifier.size(20.dp)
                )
            }
        }) else null,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AuthBlue,
            unfocusedBorderColor = AuthBorder,
            focusedLabelColor = AuthBlue,
            unfocusedLabelColor = AuthTextSecond,
            focusedContainerColor = AuthSurface,
            unfocusedContainerColor = AuthSurface,
            cursorColor = AuthBlue
        )
    )
}

/** Primary CTA button with loading state */
@Composable
fun AuthPrimaryButton(text : String, isLoading : Boolean = false, onClick : () -> Unit) {
    Button(
        onClick = { if (!isLoading) onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AuthBlue,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 1.dp
        )
    ) {
        AnimatedContent(targetState = isLoading, label = "btnContent") { loading ->
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.5.dp
                )
            } else {
                Text(text = text, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/** Horizontal divider with "OR" label */
@Composable
fun AuthDivider() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = AuthBorder)
        Text("OR", fontSize = 11.sp, color = AuthTextSecond, fontWeight = FontWeight.SemiBold)
        HorizontalDivider(modifier = Modifier.weight(1f), color = AuthBorder)
    }
}

/** Bottom navigation link row */
@Composable
fun AuthFooterRow(question: String, actionLabel: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = question, fontSize = 14.sp, color = AuthTextSecond)
        TextButton(onClick = onClick, contentPadding = PaddingValues(horizontal = 6.dp)) {
            Text(
                text = actionLabel,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AuthBlue
            )
        }
    }
}

/** Red error banner */
@Composable
fun ErrorBanner(message: String, onDismiss: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(12.dp),
        color = AuthError.copy(alpha = 0.08f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = AuthError,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = message,
                fontSize = 13.sp,
                color = AuthError,
                modifier = Modifier.weight(1f).padding(horizontal = 10.dp)
            )
            IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Close, contentDescription = null, tint = AuthError, modifier = Modifier.size(16.dp))
            }
        }
    }
}

/** Password strength bar */
@Composable
fun PasswordStrengthIndicator(password: String) {
    val strength = when {
        password.length < 6 -> 0 // Weak
        password.length < 10 || !password.any { it.isDigit() }  -> 1 // Fair
        password.any { it.isUpperCase() } && password.any { it.isDigit() } && password.length >= 10 -> 3 // Strong
        else -> 2 // Good
    }

    val (label, color) = when (strength) {
        0 -> "Weak" to Color(0xFFEF4444)
        1 -> "Fair" to Color(0xFFF59E0B)
        2 -> "Good" to Color(0xFF3B82F6)
        else -> "Strong" to Color(0xFF10B981)
    }

    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(4) { i ->
                val filled = i <= strength
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (filled) color else AuthBorder)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Password strength: $label",
            fontSize = 11.sp,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}