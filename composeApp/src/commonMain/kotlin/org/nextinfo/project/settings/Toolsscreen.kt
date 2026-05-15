package org.nextinfo.project.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.nextinfo.project.Blue100
import org.nextinfo.project.Blue500
import org.nextinfo.project.Gray100
import org.nextinfo.project.Gray200
import org.nextinfo.project.Gray600
import org.nextinfo.project.Gray900
import org.nextinfo.project.PageBG
import org.nextinfo.project.PureWhite

enum class AppLanguage(val label: String, val native: String, val flag: String) {
    ENGLISH("English", "English", "🇬🇧"),
    SWAHILI("Swahili", "Kiswahili", "🇹🇿"),
    FRENCH( "French", "Français", "🇫🇷"),
    ARABIC( "Arabic", "العربية", "🇸🇦"),
    PORTUGUESE("Portuguese", "Português", "🇧🇷")
}

enum class AppTheme(val label: String, val icon: ImageVector) {
    LIGHT( "Light", Icons.Default.LightMode),
    DARK(  "Dark", Icons.Default.DarkMode),
    SYSTEM("System", Icons.Default.SettingsBrightness)
}

enum class TextSize(val label: String, val scale: Float) {
    SMALL( "Small", 0.85f),
    MEDIUM("Medium", 1.00f),
    LARGE( "Large", 1.15f)
}

enum class FeedLayout(val label: String, val icon: ImageVector) {
    LIST("List", Icons.Default.ViewList),
    GRID("Grid", Icons.Default.GridView),
    COMPACT("Compact", Icons.Default.DensitySmall)
}

data class AppSettings(
    val language: AppLanguage = AppLanguage.ENGLISH,
    val theme: AppTheme = AppTheme.SYSTEM,
    val textSize: TextSize = TextSize.MEDIUM,
    val feedLayout: FeedLayout = FeedLayout.LIST,
    val pushEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val doNotDisturb: Boolean = false,
    val dataLimitEnabled: Boolean = false,
    val autoplayMedia: Boolean = true,
    val analyticsEnabled: Boolean = true,
    val crashReports: Boolean = true
)


@Composable
fun ToolsContent(paddingValues: PaddingValues) {
    var settings by remember { mutableStateOf(AppSettings()) }
    var showLanguagePicker by remember { mutableStateOf(false) }
    var showThemePicker by remember { mutableStateOf(false) }
    var showTextSizePicker by remember { mutableStateOf(false) }
    var showFeedPicker by remember { mutableStateOf(false) }
    var showClearCache by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(paddingValues)) {
        ToolsScreen(
            settings = settings,
            onLanguageClick = { showLanguagePicker = true },
            onThemeClick = { showThemePicker = true },
            onTextSizeClick = { showTextSizePicker = true },
            onFeedLayoutClick = { showFeedPicker = true },
            onClearCacheClick = { showClearCache = true },
            onSettingsChange = { settings = it }
        )
    }

    if (showLanguagePicker) {
        LanguagePickerDialog(
            current = settings.language,
            onDismiss = { showLanguagePicker = false },
            onPick = { settings = settings.copy(language = it); showLanguagePicker = false }
        )
    }
    if (showThemePicker) {
        ThemePickerDialog(
            current = settings.theme,
            onDismiss = { showThemePicker = false },
            onPick = { settings = settings.copy(theme = it); showThemePicker = false }
        )
    }
    if (showTextSizePicker) {
        TextSizePickerDialog(
            current = settings.textSize,
            onDismiss = { showTextSizePicker = false },
            onPick = { settings = settings.copy(textSize = it); showTextSizePicker = false }
        )
    }
    if (showFeedPicker) {
        FeedLayoutPickerDialog(
            current = settings.feedLayout,
            onDismiss = { showFeedPicker = false },
            onPick = { settings = settings.copy(feedLayout = it); showFeedPicker = false }
        )
    }
    if (showClearCache) {
        ClearCacheDialog(
            onDismiss = { showClearCache = false },
            onConfirm = { showClearCache = false }
        )
    }
}

@Composable
private fun ToolsScreen(
    settings: AppSettings,
    onLanguageClick: () -> Unit,
    onThemeClick: () -> Unit,
    onTextSizeClick: () -> Unit,
    onFeedLayoutClick: () -> Unit,
    onClearCacheClick: () -> Unit,
    onSettingsChange: (AppSettings) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(PageBG),
        contentPadding = PaddingValues(bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        item { ToolsHero() }
        item { ToolsSectionLabel("Appearance") }
        item {
            ToolsGroup {
                ToolsNavRow(
                    icon = Icons.Default.Translate,
                    iconBg = Color(0xFF1A6BFF),
                    title = "Language",
                    value = "${settings.language.flag} ${settings.language.native}",
                    onClick = onLanguageClick
                )
                ToolsDivider()
                ToolsNavRow(
                    icon = Icons.Default.Palette,
                    iconBg = Color(0xFF6B35FF),
                    title = "Theme",
                    value = settings.theme.label,
                    onClick = onThemeClick
                )
                ToolsDivider()
                ToolsNavRow(
                    icon = Icons.Default.TextFields,
                    iconBg = Color(0xFF35B37E),
                    title = "Text Size",
                    value = settings.textSize.label,
                    onClick = onTextSizeClick
                )
                ToolsDivider()
                ToolsNavRow(
                    icon = Icons.Default.ViewList,
                    iconBg = Color(0xFFFF6B35),
                    title = "Feed Layout",
                    value = settings.feedLayout.label,
                    onClick = onFeedLayoutClick
                )
            }
        }

        item { ToolsSectionLabel("Notifications") }
        item {
            ToolsGroup {
                ToolsToggleRow(
                    icon = Icons.Default.Notifications,
                    iconBg = Color(0xFF1A6BFF),
                    title = "Push Notifications",
                    subtitle = "Receive alerts from subscribed companies",
                    checked = settings.pushEnabled,
                    onToggle = { onSettingsChange(settings.copy(pushEnabled = it)) }
                )
                ToolsDivider()
                ToolsToggleRow(
                    icon = Icons.Default.VolumeUp,
                    iconBg = Color(0xFF35B37E),
                    title = "Sound",
                    subtitle = "Play sound on new notifications",
                    checked = settings.soundEnabled,
                    onToggle = { onSettingsChange(settings.copy(soundEnabled = it)) }
                )
                ToolsDivider()
                ToolsToggleRow(
                    icon = Icons.Default.Vibration,
                    iconBg = Color(0xFFFF9F0A),
                    title = "Vibration",
                    subtitle = "Vibrate on new notifications",
                    checked = settings.vibrationEnabled,
                    onToggle = { onSettingsChange(settings.copy(vibrationEnabled = it)) }
                )
                ToolsDivider()
                ToolsToggleRow(
                    icon = Icons.Default.DoNotDisturb,
                    iconBg = Color(0xFFFF3B30),
                    title = "Do Not Disturb",
                    subtitle = "Mute all notifications temporarily",
                    checked = settings.doNotDisturb,
                    onToggle = { onSettingsChange(settings.copy(doNotDisturb = it)) }
                )
            }
        }

        item { ToolsSectionLabel("Data & Storage") }
        item {
            ToolsGroup {
                ToolsToggleRow(
                    icon = Icons.Default.DataSaverOn,
                    iconBg = Color(0xFF35B37E),
                    title = "Data Saver",
                    subtitle = "Limit background data usage",
                    checked = settings.dataLimitEnabled,
                    onToggle = { onSettingsChange(settings.copy(dataLimitEnabled = it)) }
                )
                ToolsDivider()
                ToolsToggleRow(
                    icon = Icons.Default.PlayCircle,
                    iconBg = Color(0xFF6B35FF),
                    title = "Autoplay Media",
                    subtitle = "Auto-play images and videos in feed",
                    checked = settings.autoplayMedia,
                    onToggle = { onSettingsChange(settings.copy(autoplayMedia = it)) }
                )
                ToolsDivider()
                ToolsNavRow(
                    icon = Icons.Default.CleaningServices,
                    iconBg = Color(0xFFFF6B35),
                    title = "Clear Cache",
                    value = "42.3 MB",
                    onClick = onClearCacheClick
                )
            }
        }

        item { ToolsSectionLabel("Privacy & Diagnostics") }
        item {
            ToolsGroup {
                ToolsToggleRow(
                    icon = Icons.Default.Analytics,
                    iconBg = Color(0xFF1A6BFF),
                    title = "Usage Analytics",
                    subtitle = "Help improve the app anonymously",
                    checked = settings.analyticsEnabled,
                    onToggle = { onSettingsChange(settings.copy(analyticsEnabled = it)) }
                )
                ToolsDivider()
                ToolsToggleRow(
                    icon = Icons.Default.BugReport,
                    iconBg = Color(0xFFFF3B30),
                    title = "Crash Reports",
                    subtitle = "Send crash logs to developers",
                    checked = settings.crashReports,
                    onToggle = { onSettingsChange(settings.copy(crashReports = it)) }
                )
            }
        }

        item { ToolsSectionLabel("About") }
        item {
            ToolsGroup {
                ToolsNavRow(
                    icon = Icons.Default.Policy,
                    iconBg = Color(0xFF48484A),
                    title = "Privacy Policy",
                    value = "",
                    onClick = {}
                )
                ToolsDivider()
                ToolsNavRow(
                    icon = Icons.Default.Gavel,
                    iconBg = Color(0xFF48484A),
                    title = "Terms of Service",
                    value = "",
                    onClick = {}
                )
                ToolsDivider()
                ToolsNavRow(
                    icon = Icons.Default.StarRate,
                    iconBg = Color(0xFFFF9F0A),
                    title = "Rate NextInfo",
                    value = "",
                    onClick = {}
                )
                ToolsDivider()
                // Version row — no chevron
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Blue500),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Version", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Gray900)
                        Text("NextInfo 1.0.0", fontSize = 12.sp, color = Gray600)
                    }
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF35B37E).copy(alpha = 0.12f)
                    ) {
                        Text(
                            "Up to date",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 10.sp,
                            color = Color(0xFF35B37E),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}


@Composable
private fun ToolsHero() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(listOf(Color(0xFF1355D6), Color(0xFF5B9BFF)))
            )
            .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 28.dp)
    ) {
        // Decorative bubbles
        Box(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.TopEnd)
                .offset(x = 36.dp, y = (-36).dp)
                .background(Color.White.copy(alpha = 0.06f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-12).dp, y = 20.dp)
                .background(Color.White.copy(alpha = 0.04f), CircleShape)
        )

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(
                        text = "Settings",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Personalise your NextInfo experience",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Quick status pills
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickPill(icon = Icons.Default.DarkMode, label = "System theme")
                QuickPill(icon = Icons.Default.Translate, label = "English")
                QuickPill(icon = Icons.Default.Notifications, label = "Alerts on")
            }
        }
    }
}

@Composable
private fun QuickPill(icon: ImageVector, label: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(icon, contentDescription = null, tint = Color.White.copy(alpha = 0.9f), modifier = Modifier.size(12.dp))
            Text(label, fontSize = 11.sp, color = Color.White.copy(alpha = 0.9f), fontWeight = FontWeight.Medium)
        }
    }
}


@Composable
private fun ToolsSectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Gray600.copy(alpha = 0.6f),
        letterSpacing = 1.2.sp,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 6.dp)
    )
}


@Composable
private fun ToolsGroup(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = PureWhite,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.border(1.dp, Gray200, RoundedCornerShape(16.dp)),
            content  = content
        )
    }
}

@Composable
private fun ToolsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 66.dp, end = 16.dp),
        thickness = 0.5.dp,
        color = Gray200
    )
}


@Composable
private fun ToolsNavRow(
    icon: ImageVector,
    iconBg: Color,
    title: String,
    value: String,
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
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        }

        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Gray900,
            modifier = Modifier.weight(1f)
        )

        if (value.isNotBlank()) {
            Text(text = value, fontSize = 13.sp, color = Gray600)
            Spacer(modifier = Modifier.width(4.dp))
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Gray200,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun ToolsToggleRow(
    icon: ImageVector,
    iconBg: Color,
    title: String,
    subtitle: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onToggle(!checked) }
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title,    fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Gray900)
            Text(text = subtitle, fontSize = 11.sp, color = Gray600, lineHeight = 16.sp)
        }

        AnimatedSwitch(checked = checked, onToggle = onToggle)
    }
}

@Composable
private fun AnimatedSwitch(checked: Boolean, onToggle: (Boolean) -> Unit) {
    val trackColor by animateColorAsState(
        targetValue = if (checked) Blue500 else Gray200,
        animationSpec = tween(200),
        label = "switchTrack"
    )
    val thumbOffset by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "switchThumb"
    )
    val thumbScale by animateFloatAsState(
        targetValue = if (checked) 1f else 0.85f,
        animationSpec = spring(dampingRatio = 0.5f),
        label = "thumbScale"
    )

    Box(
        modifier = Modifier
            .width(46.dp)
            .height(26.dp)
            .clip(CircleShape)
            .background(trackColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onToggle(!checked) }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .padding(start = (3 + thumbOffset * 20).dp, end = 3.dp)
                .size(20.dp)
                .scale(thumbScale)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}


@Composable
private fun LanguagePickerDialog(
    current: AppLanguage,
    onDismiss: () -> Unit,
    onPick: (AppLanguage) -> Unit
) {
    ToolsDialog(title = "Language", onDismiss = onDismiss) {
        AppLanguage.entries.forEach { lang ->
            val selected = lang == current
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (selected) Blue100 else Color.Transparent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onPick(lang) }
                    )
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(lang.flag, fontSize = 22.sp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(lang.native, fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                        color = if (selected) Blue500 else Gray900)
                    Text(lang.label, fontSize = 11.sp, color = Gray600)
                }
                if (selected) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null,
                        tint = Blue500, modifier = Modifier.size(20.dp))
                }
            }
            if (lang != AppLanguage.entries.last()) {
                HorizontalDivider(color = Gray200, thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 4.dp))
            }
        }
    }
}


@Composable
private fun ThemePickerDialog(
    current: AppTheme,
    onDismiss: () -> Unit,
    onPick: (AppTheme) -> Unit
) {
    ToolsDialog(title = "Theme", onDismiss = onDismiss) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AppTheme.entries.forEach { theme ->
                val selected = theme == current
                val bgColor by animateColorAsState(
                    targetValue = if (selected) Blue500 else Gray100,
                    animationSpec = tween(200),
                    label = "themeBg"
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(bgColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onPick(theme) }
                        )
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        theme.icon,
                        contentDescription = null,
                        tint = if (selected) Color.White else Gray600,
                        modifier = Modifier.size(26.dp)
                    )
                    Text(
                        text = theme.label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selected) Color.White else Gray600
                    )
                }
            }
        }
    }
}


@Composable
private fun TextSizePickerDialog(
    current: TextSize,
    onDismiss: () -> Unit,
    onPick: (TextSize) -> Unit
) {
    ToolsDialog(title = "Text Size", onDismiss = onDismiss) {
        // Live preview
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Blue100,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "The quick brown fox jumps over the lazy dog.",
                fontSize = (14 * current.scale).sp,
                color = Blue500,
                modifier = Modifier.padding(14.dp),
                lineHeight = (20 * current.scale).sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Size options
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextSize.entries.forEach { size ->
                val selected = size == current
                val bg by animateColorAsState(
                    targetValue = if (selected) Blue500 else Gray100,
                    animationSpec = tween(200), label = "sizeBg"
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(bg)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onPick(size) }
                        )
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = size.label,
                        fontSize = (12 * size.scale).sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selected) Color.White else Gray600
                    )
                }
            }
        }
    }
}


@Composable
private fun FeedLayoutPickerDialog(
    current: FeedLayout,
    onDismiss: () -> Unit,
    onPick: (FeedLayout) -> Unit
) {
    ToolsDialog(title = "Feed Layout", onDismiss = onDismiss) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FeedLayout.entries.forEach { layout ->
                val selected = layout == current
                val bg by animateColorAsState(
                    targetValue = if (selected) Blue500 else Gray100,
                    animationSpec = tween(200), label = "layoutBg"
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(bg)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onPick(layout) }
                        )
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        layout.icon,
                        contentDescription = null,
                        tint = if (selected) Color.White else Gray600,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        layout.label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selected) Color.White else Gray600
                    )
                }
            }
        }
    }
}


@Composable
private fun ClearCacheDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    ToolsDialog(title = "Clear Cache", onDismiss = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFFF6B35).copy(alpha = 0.08f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Default.CleaningServices, contentDescription = null,
                    tint = Color(0xFFFF6B35), modifier = Modifier.size(20.dp))
                Text(
                    text = "This will remove 42.3 MB of cached images and data. " + "The app may load slightly slower until new content is cached.",
                    fontSize = 12.sp,
                    color = Color(0xFFFF6B35),
                    lineHeight = 18.sp
                )
            }
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
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B35))
            ) { Text("Clear", fontWeight = FontWeight.Bold) }
        }
    }
}


@Composable
private fun ToolsDialog(
    title: String,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            color = PureWhite
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900
                )
                Spacer(modifier = Modifier.height(18.dp))
                content()
            }
        }
    }
}