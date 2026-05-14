package org.nextinfo.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import kotlin.math.abs
import kotlin.random.Random


enum class NotificationType { IMAGE_ONLY, IMAGE_TEXT, TEXT_ONLY, REMINDER, PROMOTION }

data class CompanyNotification(
    val id : String,
    val type : NotificationType,
    val title : String? = null,
    val body : String? = null,
    val imageUrl : String? = null,
    val badge : NotificationBadge,
    val timeAgo : String
)

data class NotificationBadge(
    val label : String,
    val color : Color,
    val icon  : ImageVector
)


private val badgeUpdate   = NotificationBadge("Update", Color(0xFF1A6BFF), Icons.Default.Info)
private val badgePromo    = NotificationBadge("Promo", Color(0xFFFF6B35), Icons.Default.Star)
private val badgeReminder = NotificationBadge("Reminder", Color(0xFF6B35FF), Icons.Default.Notifications)
private val badgeCampaign = NotificationBadge("Campaign", Color(0xFF35B37E), Icons.Default.Campaign)
private val badgeMedia    = NotificationBadge("Media", Color(0xFFFF3561), Icons.Default.Image)

private val sampleImageUrls = listOf(
    "https://picsum.photos/id/1018/600/300",
    "https://picsum.photos/id/1025/600/300",
    "https://picsum.photos/id/1035/600/300",
    "https://picsum.photos/id/1040/600/300",
    "https://picsum.photos/id/1043/600/300",
    "https://picsum.photos/id/1050/600/300",
    "https://picsum.photos/id/1062/600/300",
    "https://picsum.photos/id/1080/600/300"
)

private val timeLabels = listOf(
    "Just now", "5 min ago", "1 hour ago",
    "3 hours ago", "Yesterday", "2 days ago", "1 week ago"
)

fun generateNotificationsFor(company: Company): List<CompanyNotification> {
    val rng = Random(abs(company.name.hashCode()))
    fun img() = sampleImageUrls[rng.nextInt(sampleImageUrls.size)]
    fun time() = timeLabels[rng.nextInt(timeLabels.size)]
    val discount = rng.nextInt(40) + 10
    val bonus = rng.nextInt(3) + 1
    val ofaDay = if (rng.nextBoolean()) "kesho" else "Ijumaa"
    val miadi = if (rng.nextBoolean()) "kesho saa tatu asubuhi" else "Jumanne saa kumi na moja"

    return listOf(
        CompanyNotification(
            id = "n1", type = NotificationType.TEXT_ONLY,
            title = "Matangazo Mapya – ${company.name}",
            body = "${company.name} inawasilisha huduma mpya kwa wateja wote. Jiunge leo na upate faida za kipekee.",
            badge = badgeUpdate, timeAgo = timeLabels[0]
        ),
        CompanyNotification(
            id = "n2", type = NotificationType.TEXT_ONLY,
            title = "Kikumbusha cha Muhimu",
            body = "Usisahau! Ofa ya ${company.name} inaisha $ofaDay. Fanya malipo yako sasa.",
            badge = badgeReminder, timeAgo = timeLabels[1]
        ),
        CompanyNotification(
            id = "n3", type = NotificationType.IMAGE_ONLY,
            imageUrl = img(), badge = badgeMedia, timeAgo = time()
        ),
        CompanyNotification(
            id = "n4", type = NotificationType.IMAGE_ONLY,
            imageUrl = img(), badge = badgeCampaign, timeAgo = time()
        ),
        CompanyNotification(
            id = "n5", type = NotificationType.IMAGE_TEXT,
            title = "Ofa Maalum - Punguzo la $discount%",
            body = "Pata punguzo kubwa kwenye bidhaa na huduma za ${company.name}. Ofa hii ni ya muda mfupi tu.",
            imageUrl = img(), badge = badgePromo, timeAgo = time()
        ),
        CompanyNotification(
            id = "n6", type = NotificationType.IMAGE_TEXT,
            title = "Uzinduzi Rasmi",
            body = "${company.name} inazindua rasmi tawi jipya. Karibu na upate huduma bora zaidi.",
            imageUrl = img(), badge = badgeCampaign, timeAgo = time()
        ),
        CompanyNotification(
            id = "n7", type = NotificationType.REMINDER,
            title = "Kumbuka Miadi Yako",
            body = "Una miadi na ${company.name} $miadi. Fika mapema ili kupata huduma bora.",
            badge = badgeReminder, timeAgo = time()
        ),
        CompanyNotification(
            id = "n8", type = NotificationType.PROMOTION,
            title = "Bonus Maalum kwa Wateja Wapya",
            body = "Kama mteja mpya wa ${company.name}, unapata zawadi ya bure kwa miezi $bonus ya kwanza.",
            badge = badgePromo, timeAgo = time()
        )
    )
}


@Composable
fun CompanyProfileScreen(company: Company, onBack: () -> Unit) {
    val notifications = remember(company.name) { generateNotificationsFor(company) }
    val listState = rememberLazyListState()
    val isScrolled by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }

    Scaffold(
        containerColor = PageBG,
        topBar = {
            CompanyProfileTopBar(company = company, isScrolled = isScrolled, onBack = onBack)
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier.padding(paddingValues).fillMaxSize()
        ) {
            item { CompanyProfileHeader(company = company) }
            item { SectionLabel(text = "News & Updates") }

            itemsIndexed(notifications, key = { _, n -> n.id }) { index, notification ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) { visible = true }
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(240, delayMillis = index * 50)) + slideInVertically(tween(240, delayMillis = index * 50)) { it / 4 }
                ) {
                    NotificationCard(
                        notification = notification,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}


@Composable
private fun CompanyProfileTopBar(company: Company, isScrolled: Boolean, onBack: () -> Unit) {
    val elevation by animateFloatAsState(
        targetValue = if (isScrolled) 4f else 0f,
        animationSpec = tween(220),
        label = "topBarElevation"
    )
    Surface(shadowElevation = elevation.dp, color = PureWhite) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Gray900)
            }
            CompanyLogo(company = company, size = 32, cornerRadius = 8)
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = company.name, fontSize = 15.sp, fontWeight = FontWeight.Bold,
                    color = Gray900, maxLines = 1, overflow = TextOverflow.Ellipsis
                )
                Text(text = "${company.subscribers} subscribers", fontSize = 11.sp, color = Gray600)
            }
            AnimatedVisibility(visible = isScrolled) {
                TextButton(onClick = {}, contentPadding = PaddingValues(horizontal = 12.dp)) {
                    Text("Subscribe", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Blue500)
                }
            }
        }
    }
}


@Composable
private fun CompanyProfileHeader(company: Company) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(listOf(Color(0xFF1355D6), Color(0xFF5B9BFF))))
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 24.dp)
    ) {
        Box(
            modifier = Modifier.size(130.dp).align(Alignment.TopEnd)
                .offset(x = 36.dp, y = (-36).dp)
                .background(Color.White.copy(alpha = 0.06f), CircleShape)
        )
        Box(
            modifier = Modifier.size(70.dp).align(Alignment.BottomStart)
                .offset(x = (-16).dp, y = 24.dp)
                .background(Color.White.copy(alpha = 0.04f), CircleShape)
        )
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CompanyLogo(company = company, size = 68, cornerRadius = 20, onDark = true)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = company.name, color = Color.White,
                        fontWeight = FontWeight.ExtraBold, fontSize = 20.sp,
                        maxLines = 2, overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Person, contentDescription = null,
                            tint = Color.White.copy(alpha = 0.65f), modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "${company.subscribers} subscribers",
                            color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(shape = RoundedCornerShape(20.dp), color = Color.White.copy(alpha = 0.15f)) {
                        Text(
                            text = company.category,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                            fontSize = 10.sp, color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            TagChipsRow(tags = company.tags, onDark = true)
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, contentColor = Color(0xFF1355D6)
                ),
                contentPadding = PaddingValues(vertical = 13.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Subscribe", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun NotificationCard(notification: CompanyNotification, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = PureWhite
    ) {
        Column(modifier = Modifier.border(1.dp, Gray200, RoundedCornerShape(16.dp))) {
            when (notification.type) {
                NotificationType.IMAGE_ONLY -> {
                    NotificationImageBanner(imageUrl = notification.imageUrl ?: "")
                    NotificationFooter(notification = notification)
                }
                NotificationType.IMAGE_TEXT -> {
                    NotificationImageBanner(imageUrl = notification.imageUrl ?: "")
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            NotificationBadgeChip(badge = notification.badge)
                            Text(notification.timeAgo, fontSize = 10.sp, color = Gray600.copy(alpha = 0.55f))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        notification.title?.let {
                            Text(it, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Gray900)
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        notification.body?.let {
                            Text(it, fontSize = 12.sp, color = Gray600, lineHeight = 18.sp)
                        }
                    }
                }
                NotificationType.TEXT_ONLY,
                NotificationType.REMINDER,
                NotificationType.PROMOTION -> {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .defaultMinSize(minHeight = 1.dp)
                                .background(
                                    notification.badge.color,
                                    RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                                )
                        )
                        Column(
                            modifier = Modifier.weight(1f)
                                .padding(horizontal = 14.dp, vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                NotificationBadgeChip(badge = notification.badge)
                                Text(notification.timeAgo, fontSize = 10.sp, color = Gray600.copy(alpha = 0.55f))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            notification.title?.let {
                                Text(it, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Gray900)
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            notification.body?.let {
                                Text(it, fontSize = 12.sp, color = Gray600, lineHeight = 18.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun NotificationImageBanner(imageUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(Gray100),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isNotBlank()) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(listOf(Gray100, Gray200, Gray100))
                            )
                    )
                },
                error = {
                    Box(
                        modifier = Modifier.fillMaxSize().background(Gray100),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.BrokenImage,
                                contentDescription = null,
                                tint = Gray200,
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                text = "Image unavailable",
                                fontSize = 11.sp,
                                color = Gray600.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            )
        }
    }
}


@Composable
private fun NotificationFooter(notification: CompanyNotification) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NotificationBadgeChip(badge = notification.badge)
        Text(text = notification.timeAgo, fontSize = 10.sp, color = Gray600.copy(alpha = 0.55f))
    }
}


@Composable
private fun NotificationBadgeChip(badge: NotificationBadge) {
    Surface(shape = RoundedCornerShape(6.dp), color = badge.color.copy(alpha = 0.10f)) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = badge.icon,
                contentDescription = null,
                tint = badge.color,
                modifier = Modifier.size(11.dp)
            )
            Text(text = badge.label, fontSize = 10.sp, color = badge.color, fontWeight = FontWeight.Bold)
        }
    }
}