package org.nextinfo.project.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun RegisterScreen(onRegisterSuccess : () -> Unit, onLogin : () -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current
    val scroll = rememberScrollState()

    AuthScaffold(scrollState = scroll) {
        AuthBrand()
        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Create Account",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AuthTextPrimary
        )
        Text(
            text = "Join thousands of subscribers",
            fontSize = 14.sp,
            color = AuthTextSecond,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        AnimatedVisibility(visible = errorMsg != null) {
            ErrorBanner(message = errorMsg ?: "") { errorMsg = null }
        }

        // Full name
        AuthTextField(
            value = fullName,
            onValueChange = { fullName = it; errorMsg = null },
            label = "Full Name",
            leadingIcon = Icons.Outlined.Person,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Email
        AuthTextField(
            value = email,
            onValueChange = { email = it; errorMsg = null },
            label = "Email Address",
            leadingIcon = Icons.Outlined.Email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Phone
        AuthTextField(
            value = phone,
            onValueChange = { phone = it; errorMsg = null },
            label = "Phone Number",
            leadingIcon = Icons.Outlined.Phone,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Password
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

        // Password strength
        if (password.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            PasswordStrengthIndicator(password = password)
        }

        Spacer(modifier = Modifier.height(24.dp))

        AuthPrimaryButton(
            text = "Create Account",
            isLoading = isLoading,
            onClick = {
                when {
                    fullName.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank() ->
                        errorMsg = "Please fill in all fields"
                    !email.contains("@") ->
                        errorMsg = "Please enter a valid email"
                    password.length < 6 ->
                        errorMsg = "Password must be at least 6 characters"
                    else -> {
                        isLoading = true
                        onRegisterSuccess()
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
        AuthDivider()
        Spacer(modifier = Modifier.height(20.dp))

        AuthFooterRow(
            question = "Already have an account?",
            actionLabel = "Sign In",
            onClick = onLogin
        )
    }
}
