package org.nextinfo.project.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun LoginScreen(
    onLoginSuccess : () -> Unit,
    onRegister : () -> Unit,
    onForgotPass : () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current

    AuthScaffold {
        // Logo
        AuthBrand()
        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Welcome back",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AuthTextPrimary
        )
        Text(
            text = "Sign in to continue",
            fontSize = 14.sp,
            color = AuthTextSecond,
            modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
        )

        // Error banner
        AnimatedVisibility(visible = errorMsg != null) {
            ErrorBanner(message = errorMsg ?: "") { errorMsg = null }
        }

        // Fields
        AuthTextField(
            value = username,
            onValueChange = { username = it; errorMsg = null },
            label = "Username or Email",
            leadingIcon = Icons.Outlined.Person,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthTextField(
            value = password,
            onValueChange = { password = it; errorMsg = null },
            label = "Password",
            leadingIcon = Icons.Outlined.Lock,
            isPassword = true,
            passwordVisible = passVisible,
            onTogglePass = { passVisible = !passVisible },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        // Forgot password
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            TextButton(onClick = onForgotPass) {
                Text(
                    text = "Forgot password?",
                    fontSize = 13.sp,
                    color = AuthBlue,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Primary button
        AuthPrimaryButton(
            text = "Sign In",
            isLoading = isLoading,
            onClick = {
                if (username.isBlank() || password.isBlank()) {
                    errorMsg = "Please fill in all fields"
                } else {
                    isLoading = true
                    // TODO: real auth — simulate delay
                    onLoginSuccess()
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
        AuthDivider()
        Spacer(modifier = Modifier.height(20.dp))
        AuthFooterRow(
            question = "Don't have an account?",
            actionLabel = "Register",
            onClick = onRegister
        )
    }
}