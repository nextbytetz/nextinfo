package org.nextinfo.project.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.nextinfo.project.Blue100
import org.nextinfo.project.Blue500
import org.nextinfo.project.Company
import org.nextinfo.project.Gray100
import org.nextinfo.project.Gray200
import org.nextinfo.project.Gray600
import org.nextinfo.project.Gray900
import org.nextinfo.project.PageBG
import org.nextinfo.project.PureWhite
import org.nextinfo.project.SectionLabel


enum class NotificationChannel(
    val label: String,
    val description: String,
    val icon: ImageVector
) {
    EMAIL(    "Email only",    "Receive updates via email",        Icons.Default.Email),
    SMS(      "SMS only",      "Receive updates via text message", Icons.Default.Sms),
    WHATSAPP( "WhatsApp only", "Receive updates via WhatsApp",     Icons.Default.Chat),
    ALL(      "All channels",  "Email, SMS and WhatsApp",          Icons.Default.Notifications)
}

data class UserProfile(
    val username: String = "developer_samile",
    val name: String = "Developer Samile",
    val email: String = "y.samile@nextbyte.co.tz",
    val phone: String = "+255 620 350 083",
    val notifChannel: NotificationChannel = NotificationChannel.EMAIL
)


@Composable
fun ProfileContent(paddingValues: PaddingValues, ){
    var profile by remember { mutableStateOf(UserProfile()) }
    var showEditProfile by remember { mutableStateOf(false) }
    var showChangePassword by remember { mutableStateOf(false) }
    var showDeleteAccount by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(paddingValues)) {
        ProfileScreen(
            profile = profile,
            onEditProfile = { showEditProfile = true },
            onChangePassword = { showChangePassword = true },
            onDeleteAccount = { showDeleteAccount = true },
            onChannelChange = { profile = profile.copy(notifChannel = it) }
        )

        if (showEditProfile) {
            EditProfileDialog(
                profile = profile,
                onDismiss = { showEditProfile = false },
                onSave = { updated ->
                    profile = updated
                    showEditProfile = false
                }
            )
        }

        if (showChangePassword) {
            ChangePasswordDialog(
                onDismiss = { showChangePassword = false },
                onSave = { showChangePassword = false }
            )
        }

        if (showDeleteAccount) {
            DeleteAccountDialog(
                onDismiss = { showDeleteAccount = false },
                onConfirm = {
                    showDeleteAccount = false /* todo hook to backend */
                }
            )
        }
    }
}

@Composable
private fun ProfileScreen(
    profile: UserProfile,
    onEditProfile: () -> Unit,
    onChangePassword: () -> Unit,
    onDeleteAccount: () -> Unit,
    onChannelChange: (NotificationChannel) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(PageBG),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        item { ProfileHero(profile = profile) }
        item {
            SectionLabel(
                text = "Account",
            )
        }
        item {
            SettingsGroup {
                SettingsRow(
                    icon = Icons.Default.Edit,
                    iconBg = Color(0xFF1A6BFF),
                    title = "Edit Profile",
                    subtitle = profile.username,
                    onClick = onEditProfile
                )
                SettingsDivider()
                SettingsRow(
                    icon = Icons.Default.Lock,
                    iconBg = Color(0xFF6B35FF),
                    title = "Change Password",
                    subtitle = "Update your password",
                    onClick = onChangePassword
                )
            }
        }

        item {
            SectionLabel(
                text = "Notifications",
            )
        }
        item {
            NotificationChannelGroup(
                selected  = profile.notifChannel,
                onChange  = onChannelChange
            )
        }

        item {
            SectionLabel(
                text = "Danger Zone",
            )
        }
        item {
            SettingsGroup {
                SettingsRow(
                    icon = Icons.Default.DeleteForever,
                    iconBg = Color(0xFFFF3B30),
                    title = "Delete Account",
                    subtitle = "Permanently remove your account",
                    titleColor = Color(0xFFFF3B30),
                    showChevron = true,
                    onClick = onDeleteAccount
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            SettingsGroup {
                SettingsRow(
                    icon = Icons.Default.Info,
                    iconBg = Gray600,
                    title = "Version",
                    subtitle = "NextInfo 1.0.0",
                    showChevron = false,
                    onClick  = {}
                )
            }
        }
    }
}

@Composable
private fun ProfileHero(profile: UserProfile) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(listOf(Color(0xFF1355D6), Color(0xFF5B9BFF)))
            )
            .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 28.dp)
    ) {
        // Decorative bubble
        Box(
            modifier = Modifier
                .size(110.dp)
                .align(Alignment.TopEnd)
                .offset(x = 30.dp, y = (-30).dp)
                .background(Color.White.copy(alpha = 0.06f), CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Avatar circle with initials
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = profile.name.split(" ").take(2).joinToString("") { it.first().uppercaseChar().toString() },
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = profile.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "@${profile.username}",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.75f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = profile.email,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun SettingsGroup(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        color = PureWhite,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.border(1.dp, Gray200, RoundedCornerShape(16.dp)),
            content = content
        )
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    iconBg: Color,
    title: String,
    subtitle: String,
    titleColor: Color  = Gray900,
    showChevron: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Coloured icon pill
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = titleColor
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Gray600
            )
        }

        if (showChevron) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Gray200,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 66.dp, end = 16.dp),
        thickness = 0.5.dp,
        color = Gray200
    )
}

@Composable
private fun NotificationChannelGroup(selected: NotificationChannel, onChange: (NotificationChannel) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        color = PureWhite,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.border(1.dp, Gray200, RoundedCornerShape(16.dp))
        ) {
            NotificationChannel.entries.forEachIndexed { index, channel ->
                NotificationChannelRow(
                    channel = channel,
                    isSelected = channel == selected,
                    onClick = { onChange(channel) }
                )
                if (index < NotificationChannel.entries.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 66.dp, end = 16.dp),
                        thickness = 0.5.dp,
                        color = Gray200
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationChannelRow(
    channel: NotificationChannel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) Blue100 else Color.Transparent,
        animationSpec = tween(200),
        label = "channelBg"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) Blue500 else Gray100),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = channel.icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else Gray600,
                modifier = Modifier.size(18.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = channel.label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) Blue500 else Gray900
            )
            Text(
                text = channel.description,
                fontSize = 12.sp,
                color = Gray600
            )
        }

        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Blue500,
                unselectedColor = Gray200
            )
        )
    }
}

@Composable
private fun EditProfileDialog(
    profile: UserProfile,
    onDismiss: () -> Unit,
    onSave: (UserProfile) -> Unit
) {
    var username by remember { mutableStateOf(profile.username) }
    var name by remember { mutableStateOf(profile.name) }
    var email by remember { mutableStateOf(profile.email) }
    var phone by remember { mutableStateOf(profile.phone) }

    // Simple inline validators
    val emailValid = email.contains("@") && email.contains(".")
    val phoneValid = phone.length >= 9
    val usernameValid = username.length >= 3
    val canSave = emailValid && phoneValid && usernameValid && name.isNotBlank()

    ProfileDialog(
        title = "Edit Profile",
        onDismiss = onDismiss
    ) {
        ProfileTextField(
            value = name,
            onValueChange = { name = it },
            label = "Full Name",
            leadingIcon = Icons.Default.Person
        )
        Spacer(modifier = Modifier.height(12.dp))
        ProfileTextField(
            value = username,
            onValueChange = { username = it },
            label = "Username",
            leadingIcon = Icons.Default.AlternateEmail,
            isError = username.isNotBlank() && !usernameValid,
            supportingText = if (username.isNotBlank() && !usernameValid) "Min 3 characters" else null
        )
        Spacer(modifier = Modifier.height(12.dp))
        ProfileTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            leadingIcon = Icons.Default.Email,
            keyboardType = KeyboardType.Email,
            isError = email.isNotBlank() && !emailValid,
            supportingText = if (email.isNotBlank() && !emailValid) "Enter a valid email" else null
        )
        Spacer(modifier = Modifier.height(12.dp))
        ProfileTextField(
            value = phone,
            onValueChange = { phone = it },
            label = "Phone Number",
            leadingIcon = Icons.Default.Phone,
            keyboardType = KeyboardType.Phone,
            isError = phone.isNotBlank() && !phoneValid,
            supportingText = if (phone.isNotBlank() && !phoneValid) "Enter a valid phone" else null
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Gray200)
            ) { Text("Cancel", color = Gray600) }

            Button(
                onClick  = {
                    onSave(profile.copy(
                        username = username.trim(),
                        name = name.trim(),
                        email = email.trim(),
                        phone = phone.trim()
                    ))
                },
                enabled = canSave,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue500)
            ) { Text("Save", fontWeight = FontWeight.SemiBold) }
        }
    }
}

@Composable
private fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    var current by remember { mutableStateOf("") }
    var next by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var showCurr by remember { mutableStateOf(false) }
    var showNext by remember { mutableStateOf(false) }
    var showConf by remember { mutableStateOf(false) }

    val nextValid = next.length >= 8
    val confirmMatch = next == confirm && confirm.isNotBlank()
    val canSave = current.isNotBlank() && nextValid && confirmMatch

    ProfileDialog(title = "Change Password", onDismiss = onDismiss) {
        PasswordField(
            value = current,
            onValueChange = { current = it },
            label = "Current Password",
            visible = showCurr,
            onToggle = { showCurr = !showCurr }
        )
        Spacer(modifier = Modifier.height(12.dp))
        PasswordField(
            value = next,
            onValueChange = { next = it },
            label = "New Password",
            visible = showNext,
            onToggle = { showNext = !showNext },
            isError = next.isNotBlank() && !nextValid,
            supportingText = if (next.isNotBlank() && !nextValid) "Min 8 characters" else null
        )
        Spacer(modifier = Modifier.height(12.dp))
        PasswordField(
            value = confirm,
            onValueChange = { confirm = it },
            label = "Confirm Password",
            visible = showConf,
            onToggle = { showConf = !showConf },
            isError = confirm.isNotBlank() && !confirmMatch,
            supportingText = if (confirm.isNotBlank() && !confirmMatch) "Passwords do not match" else null
        )

        // Strength indicator
        AnimatedVisibility(visible = next.isNotBlank()) {
            PasswordStrengthBar(password = next)
        }

        Spacer(modifier = Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Gray200)
            ) { Text("Cancel", color = Gray600) }

            Button(
                onClick = onSave,
                enabled = canSave,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue500)
            ) { Text("Update", fontWeight = FontWeight.SemiBold) }
        }
    }
}

@Composable
private fun DeleteAccountDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var confirmText by remember { mutableStateOf("") }
    val canDelete   = confirmText.trim().uppercase() == "DELETE"

    ProfileDialog(title = "Delete Account", onDismiss = onDismiss) {
        // Warning box
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFFF3B30).copy(alpha = 0.08f)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFFF3B30),
                    modifier = Modifier.size(20.dp).padding(top = 1.dp)
                )
                Text(
                    text = "This action is permanent and cannot be undone. " + "All your data, subscriptions and preferences will be lost.",
                    fontSize = 12.sp,
                    color = Color(0xFFFF3B30),
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text       = "Type DELETE to confirm",
            fontSize   = 12.sp,
            color      = Gray600,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        ProfileTextField(
            value         = confirmText,
            onValueChange = { confirmText = it },
            label         = "Type DELETE",
            leadingIcon   = Icons.Default.DeleteForever,
            isError       = confirmText.isNotBlank() && !canDelete
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
                onClick  = onDismiss,
                modifier = Modifier.weight(1f),
                shape    = RoundedCornerShape(12.dp),
                border   = androidx.compose.foundation.BorderStroke(1.dp, Gray200)
            ) { Text("Cancel", color = Gray600) }

            Button(
                onClick  = onConfirm,
                enabled  = canDelete,
                modifier = Modifier.weight(1f),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor        = Color(0xFFFF3B30),
                    disabledContainerColor = Color(0xFFFF3B30).copy(alpha = 0.4f)
                )
            ) { Text("Delete", fontWeight = FontWeight.Bold) }
        }
    }
}


@Composable
private fun ProfileDialog(
    title: String,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties       = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape    = RoundedCornerShape(20.dp),
            color    = PureWhite
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text       = title,
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Gray900
                )
                Spacer(modifier = Modifier.height(20.dp))
                content()
            }
        }
    }
}


@Composable
private fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean           = false,
    supportingText: String?    = null
) {
    OutlinedTextField(
        value             = value,
        onValueChange     = onValueChange,
        label             = { Text(label, fontSize = 13.sp) },
        leadingIcon       = {
            Icon(leadingIcon, contentDescription = null, tint = if (isError) Color(0xFFFF3B30) else Blue500, modifier = Modifier.size(18.dp))
        },
        isError           = isError,
        supportingText    = supportingText?.let { { Text(it, fontSize = 11.sp) } },
        keyboardOptions   = KeyboardOptions(keyboardType = keyboardType),
        singleLine        = true,
        modifier          = Modifier.fillMaxWidth(),
        shape             = RoundedCornerShape(12.dp),
        colors            = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = Blue500,
            unfocusedBorderColor = Gray200,
            focusedContainerColor   = PureWhite,
            unfocusedContainerColor = PureWhite,
            errorBorderColor        = Color(0xFFFF3B30)
        )
    )
}


@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visible: Boolean,
    onToggle: () -> Unit,
    isError: Boolean        = false,
    supportingText: String? = null
) {
    OutlinedTextField(
        value               = value,
        onValueChange       = onValueChange,
        label               = { Text(label, fontSize = 13.sp) },
        leadingIcon         = {
            Icon(Icons.Default.Lock, contentDescription = null, tint = Blue500, modifier = Modifier.size(18.dp))
        },
        trailingIcon        = {
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector        = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (visible) "Hide" else "Show",
                    tint               = Gray600,
                    modifier           = Modifier.size(18.dp)
                )
            }
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        isError              = isError,
        supportingText       = supportingText?.let { { Text(it, fontSize = 11.sp) } },
        keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine           = true,
        modifier             = Modifier.fillMaxWidth(),
        shape                = RoundedCornerShape(12.dp),
        colors               = OutlinedTextFieldDefaults.colors(
            focusedBorderColor      = Blue500,
            unfocusedBorderColor    = Gray200,
            focusedContainerColor   = PureWhite,
            unfocusedContainerColor = PureWhite,
            errorBorderColor        = Color(0xFFFF3B30)
        )
    )
}


private fun passwordStrength(pw: String): Pair<Float, Pair<Color, String>> {
    var score = 0
    if (pw.length >= 8)                          score++
    if (pw.any { it.isUpperCase() })             score++
    if (pw.any { it.isDigit() })                 score++
    if (pw.any { "!@#\$%^&*".contains(it) })    score++
    return when (score) {
        0, 1 -> 0.25f to (Color(0xFFFF3B30) to "Weak")
        2 -> 0.50f to (Color(0xFFFF9F0A) to "Fair")
        3 -> 0.75f to (Color(0xFF35B37E) to "Good")
        else -> 1.00f to (Color(0xFF1A6BFF) to "Strong")
    }
}

@Composable
private fun PasswordStrengthBar(password: String) {
    val (fraction, meta) = passwordStrength(password)
    val (color, label)   = meta

    val animFraction by animateFloatAsState(
        targetValue   = fraction,
        animationSpec = tween(300),
        label = "pwStrength"
    )

    Column(modifier = Modifier.padding(top = 10.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape)
                .background(Gray200)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animFraction)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(color)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Strength: $label",
            fontSize = 11.sp,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}