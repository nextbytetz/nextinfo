package org.nextinfo.project

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import kotlin.random.nextInt


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
    val icon : ImageVector
)


private val badgeUpdate = NotificationBadge("Update", Color(0xFF1A6BFF), Icons.Default.Info)
private val badgePromo = NotificationBadge("Promo", Color(0xFFFF6B35), Icons.Default.Star)
private val badgeReminder = NotificationBadge("Reminder", Color(0xFF6B35FF), Icons.Default.Notifications)
private val badgeCampaign = NotificationBadge("Campaign", Color(0xFF35B37E), Icons.Default.Campaign)
private val badgeMedia = NotificationBadge("Media", Color(0xFFFF3561), Icons.Default.Image)


private val sampleImageUrls = listOf(
    "https://source.unsplash.com/600x300/?business,office",
    "https://source.unsplash.com/600x300/?technology,phone",
    "https://source.unsplash.com/600x300/?finance,bank",
    "https://source.unsplash.com/600x300/?promotion,sale",
    "https://source.unsplash.com/600x300/?africa,tanzania"
)

private val timeLabels = listOf(
    "Just now", "5 min ago", "1 hour ago",
    "3 hours ago", "Yesterday", "2 days ago", "1 week ago"
)
fun generateNotificationsFor(company: Company): List<CompanyNotification> {
    val rng  = Random(abs(company.name.hashCode()))

    fun pickImage() = sampleImageUrls[rng.nextInt(sampleImageUrls.size)]
    fun pickTime() = timeLabels[rng.nextInt(timeLabels.size)]

    return buildList {
        add(CompanyNotification(
            id = "n1",
            type = NotificationType.TEXT_ONLY,
            title = "Matangazo Mapya - ${company.name}",
            body = "${company.name} inawasilisha huduma mpya kwa wateja wote. Jiunge leo na upate faida za kipekee ambazo hazitapatikana mahali pengine. Toleo hili la mwaka ni kwa wakati mfupi tu.",
            badge = badgeUpdate,
            timeAgo = timeLabels[0]
        ))
        add(CompanyNotification(
            id = "n2",
            type = NotificationType.TEXT_ONLY,
            title = "Kikumbusha cha Muhimu",
            body = "Usisahau! Ofa ya ${company.name} inaisha ${if (rng.nextBoolean()) "kesho" else "Ijumaa"}. Fanya malipo yako sasa ili kuhakikisha unapata huduma bila usumbufu.",
            badge = badgeReminder,
            timeAgo = timeLabels[1]
        ))

        add(CompanyNotification(
            id = "n3",
            type = NotificationType.IMAGE_ONLY,
            imageUrl = pickImage(),
            badge = badgeMedia,
            timeAgo = timeLabels[2]
        ))
        add(CompanyNotification(
            id = "n4",
            type = NotificationType.IMAGE_ONLY,
            imageUrl = pickImage(),
            badge = badgeCampaign,
            timeAgo = timeLabels[4]
        ))

        // IMAGE_TEXT notifications
        add(CompanyNotification(
            id = "n5",
            type = NotificationType.IMAGE_TEXT,
            title = "Ofa Maalum - Punguzo la ${rng.nextInt(10, 50)}%",
            body = "Pata punguzo kubwa kwenye bidhaa na huduma za ${company.name}. Ofa hii ni ya muda mfupi tu kwa wateja waliojisajili.",
            imageUrl = pickImage(),
            badge = badgePromo,
            timeAgo = timeLabels[3]
        ))
        add(CompanyNotification(
            id = "n6",
            type = NotificationType.IMAGE_TEXT,
            title = "Uzinduzi Rasmi",
            body = "${company.name} inazindua rasmi tawi jipya. Karibu na upate huduma bora zaidi karibu nawe.",
            imageUrl = pickImage(),
            badge = badgeCampaign,
            timeAgo = timeLabels[5]
        ))

        // REMINDER
        add(CompanyNotification(
            id = "n7",
            type = NotificationType.REMINDER,
            title = "Kumbuka Miadi Yako",
            body = "Una miadi na ${company.name} ${if (rng.nextBoolean()) "kesho saa tatu asubuhi" else "Jumanne saa kumi na moja"}. Fika mapema ili kupata huduma bora.",
            badge = badgeReminder,
            timeAgo = timeLabels[6]
        ))

        // PROMOTION
        add(CompanyNotification(
            id = "n8",
            type = NotificationType.PROMOTION,
            title = "🎉 Bonus Maalum kwa Wateja Wapya",
            body = "Kama mteja mpya wa ${company.name}, unapata zawadi ya bure kwa miezi ${rng.nextInt(1, 4)} ya kwanza. Thibitisha akaunti yako leo.",
            badge = badgePromo,
            timeAgo = timeLabels[2]
        ))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyProfileScreen(
    company : Company,
    onBack : () -> Unit
) {
    val notifications = remember(company.name) { generateNotificationsFor(company) }

    Scaffold(
        containerColor = PageBG,
        topBar = {
            CompanyProfileTopBar(company = company, onBack = onBack)
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            item { CompanyProfileHeader(company = company) }
            item { SectionLabel("News & Updates") }
            itemsIndexed(notifications, key = { _, n -> n.id }) { _, notification ->
                NotificationCard(notification = notification)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompanyProfileTopBar(company: Company, onBack: () -> Unit) {
    Surface(shadowElevation = 2.dp, color = PureWhite) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Gray900
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            CompanyLogo(company = company, size = 32, cornerRadius = 8)
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = company.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${company.subscribers} subscribers",
                    fontSize = 11.sp,
                    color = Gray600
                )
            }
        }
    }
}

@Composable
private fun CompanyProfileHeader(company: Company) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(listOf(Color(0xFF1A6BFF), Color(0xFF4D8EFF))),
                    RoundedCornerShape(20.dp)
                )
                .padding(20.dp)
        ) {
            // Decorative bubbles
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 24.dp, y = (-24).dp)
                    .background(Color.White.copy(alpha = 0.06f), CircleShape)
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CompanyLogo(company = company, size = 60, cornerRadius = 16, onDark = true)

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = company.name,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${company.subscribers} subscribers",
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 12.sp
                        )
                    }
                }

                TagChipsRow(tags = company.tags, onDark = true)
            }
        }
    }
}

@Composable
fun NotificationCard(notification: CompanyNotification) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = PureWhite,
    ) {
        Column(
            modifier = Modifier
                .border(1.dp, Gray200, RoundedCornerShape(16.dp))
        ) {
            when (notification.type) {
                NotificationType.IMAGE_ONLY -> {
                    NotificationImageBanner(imageUrl = notification.imageUrl!!)
                    NotificationFooter(notification = notification)
                }

                NotificationType.IMAGE_TEXT -> {
                    NotificationImageBanner(imageUrl = notification.imageUrl!!)
                    Column(modifier = Modifier.padding(14.dp)) {
                        NotificationBadgeChip(badge = notification.badge)
                        Spacer(modifier = Modifier.height(8.dp))
                        notification.title?.let {
                            Text(it, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Gray900)
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        notification.body?.let {
                            Text(it, fontSize = 12.sp, color = Gray600, lineHeight = 18.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(notification.timeAgo, fontSize = 10.sp, color = Gray600.copy(alpha = 0.6f))
                    }
                }

                NotificationType.TEXT_ONLY,
                NotificationType.REMINDER,
                NotificationType.PROMOTION -> {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            NotificationBadgeChip(badge = notification.badge)
                            Text(
                                notification.timeAgo,
                                fontSize = 10.sp,
                                color = Gray600.copy(alpha = 0.6f)
                            )
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

@Composable
private fun NotificationImageBanner(imageUrl: String) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    )
}

@Composable
private fun NotificationFooter(notification: CompanyNotification) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NotificationBadgeChip(badge = notification.badge)
        Text(notification.timeAgo, fontSize = 10.sp, color = Gray600.copy(alpha = 0.6f))
    }
}

@Composable
private fun NotificationBadgeChip(badge: NotificationBadge) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = badge.color.copy(alpha = 0.12f)
    ) {
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
            Text(
                text = badge.label,
                fontSize = 10.sp,
                color = badge.color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}