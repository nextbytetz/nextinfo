package org.nextinfo.project

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage


val Blue500 = Color(0xFF1A6BFF)
val Blue100 = Color(0xFFEBF1FF)
val Blue50 = Color(0xFFF0F5FF)
val Gray900 = Color(0xFF1C1C1E)
val Gray600 = Color(0xFF48484A)
val Gray200 = Color(0xFFE5E5EA)
val Gray100 = Color(0xFFF2F2F7)
val PureWhite = Color(0xFFFFFFFF)
val PageBG = Color(0xFFF7F8FC)

@Composable
fun NextInfoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Blue500,
            background = PageBG,
            surface = PureWhite,
            onSurface = Gray900,
            onSurfaceVariant = Gray600
        ),
        content = content
    )
}

/**
 * Renders a company logo with automatic fallback to initials.
 * Priority: logoUrl → initials.
 *
 * @param onDark  When true uses white-on-transparent palette (for FeaturedCard).
 */
@Composable
fun CompanyLogo(
    company: Company,
    size: Int,
    cornerRadius: Int = 12,
    onDark: Boolean = false
) {
    val bgColor = if (onDark) Color.White.copy(alpha = 0.18f) else Blue100
    val textColor = if (onDark) Color.White else Blue500

    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        if (!company.logoUrl.isNullOrBlank()) {
            SubcomposeAsyncImage(
                model = company.logoUrl,
                contentDescription = company.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = { InitialsText(company.name, size, textColor) },
                error = { InitialsText(company.name, size, textColor) }
            )
        } else {
            InitialsText(company.name, size, textColor)
        }
    }
}

@Composable
private fun InitialsText(name: String, containerSize: Int, color: Color) {
    Text(
        text = initials(name),
        color = color,
        fontWeight = FontWeight.ExtraBold,
        fontSize = (containerSize * 0.35f).sp
    )
}


private const val MAX_VISIBLE_TAGS = 3
private const val MAX_TAG_CHARS = 12

/**
 * Shows up to [MAX_VISIBLE_TAGS] tags, truncates long tag text,
 * and shows a "+N" overflow badge for the rest.
 */
@Composable
fun TagChipsRow(tags: List<String>, onDark: Boolean = false) {
    val visible  = tags.take(MAX_VISIBLE_TAGS)
    val overflow = tags.size - visible.size

    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        visible.forEach { tag ->
            val label = if (tag.length > MAX_TAG_CHARS) tag.take(MAX_TAG_CHARS) + "…" else tag
            TagChip(text = label, onDark = onDark)
        }
        if (overflow > 0) TagChip(text = "+$overflow", onDark = onDark)
    }
}

@Composable
fun TagChip(text: String, onDark: Boolean = false) {
    val bg = if (onDark) Color.White.copy(alpha = 0.15f) else Blue100
    val fg = if (onDark) Color.White.copy(alpha = 0.9f)  else Blue500

    Surface(shape = RoundedCornerShape(5.dp), color = bg) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
            fontSize = 9.sp,
            color = fg,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}


/**
 * Horizontal pager for the top [FEATURED_COUNT] companies.
 * Animated dot indicators sit below the pager.
 */

@Composable
fun FeaturedSlider(
    companies : List<Company>,
    subscribed : Set<String>,
    onToggle : (String) -> Unit,
    onCompanyClick : (Company) -> Unit
) {
    val featured = companies.take(FEATURED_COUNT)
    val pagerState = rememberPagerState(pageCount = { featured.size })

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val company = featured[page]
            FeaturedCard(
                company = company,
                isSubscribed = company.name in subscribed,
                onToggle = { onToggle(company.name) },
                onClick = { onCompanyClick(company) }
            )
        }

        // Dot indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(featured.size) { index ->
                val selected = pagerState.currentPage == index
                val width by animateFloatAsState(
                    targetValue = if (selected) 20f else 6f,
                    animationSpec = tween(200),
                    label = "dotWidth_$index"
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .height(6.dp)
                        .width(width.dp)
                        .clip(CircleShape)
                        .background(if (selected) Blue500 else Gray200)
                )
            }
        }
    }
}



@Composable
fun FeaturedCard(
    company : Company,
    isSubscribed : Boolean,
    onToggle : () -> Unit,
    onClick : () -> Unit = {}
) {
    val btnScale by animateFloatAsState(
        targetValue = if (isSubscribed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "featuredBtnScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF1A6BFF), Color(0xFF4D8EFF))))
            .clickable { onClick() }
            .padding(18.dp)
    ) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .align(Alignment.TopEnd)
                .offset(x = 30.dp, y = (-30).dp)
                .background(Color.White.copy(alpha = 0.06f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.BottomStart)
                .offset(x = 20.dp, y = 20.dp)
                .background(Color.White.copy(alpha = 0.04f), CircleShape)
        )

        Column {
            Surface(shape = RoundedCornerShape(20.dp), color = Color.White.copy(alpha = 0.18f)) {
                Text(
                    text = "Featured",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                CompanyLogo(company = company, size = 52, cornerRadius = 14, onDark = true)

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = company.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${company.subscribers} subscribers",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    TagChipsRow(tags = company.tags, onDark = true)
                }

                Button(
                    onClick = onToggle,
                    modifier = Modifier.scale(btnScale),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSubscribed) Color.White.copy(alpha = 0.2f) else Color.White,
                        contentColor = if (isSubscribed) Color.White else Blue500
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(
                        text = if (isSubscribed) "Joined" else "Subscribe",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}



@Composable
fun CompanyCard(
    company : Company,
    isSubscribed : Boolean,
    onToggle : () -> Unit,
    onClick : () -> Unit = {}
) {
    val btnScale by animateFloatAsState(
        targetValue = if (isSubscribed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "cardBtnScale"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = PureWhite,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .border(1.dp, Gray200, RoundedCornerShape(16.dp))
                .clickable { onClick() }
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CompanyLogo(company = company, size = 46, cornerRadius = 12)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = company.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Gray900,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                TagChipsRow(tags = company.tags)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Gray600,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "${company.subscribers} subscribers",
                        fontSize = 11.sp,
                        color = Gray600
                    )
                }
            }

            OutlinedButton(
                onClick = onToggle,
                modifier = Modifier.scale(btnScale),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (isSubscribed) Gray600 else Blue500
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = if (isSubscribed) Gray200 else Blue500
                ),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (isSubscribed) "Joined" else "Subscribe",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}



@Composable
fun StatsRow(totalCompanies: Int, subscribedCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatCard(number = totalCompanies.toString(), label = "Companies", modifier = Modifier.weight(1f))
        StatCard(number = subscribedCount.toString(), label = "Subscriptions", modifier = Modifier.weight(1f))
    }
}

@Composable
fun StatCard(number: String, label: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(14.dp), color = PureWhite) {
        Column(
            modifier = Modifier
                .border(1.dp, Gray200, RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Text(text = number, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Gray900)
            Text(text = label,  fontSize = 11.sp, color = Gray600)
        }
    }
}


data class FilterOption(val label: String, val value: String)

val filterOptions = listOf(
    FilterOption("All",        "ALL"),
    FilterOption("Finance",    "FINANCE"),
    FilterOption("Banking",    "BANKING"),
    FilterOption("Telecom",    "TELECOM"),
    FilterOption("Media",      "MEDIA"),
    FilterOption("Tech",       "TECH"),
    FilterOption("Government", "GOVERNMENT"),
    FilterOption("Health",     "HEALTH"),
    FilterOption("Sports",     "SPORTS"),
    FilterOption("Utilities",  "UTILITIES"),
    FilterOption("Travel",     "TRAVEL"),
    FilterOption("Insurance",  "INSURANCE"),
    FilterOption("Logistics",  "LOGISTICS"),
    FilterOption("FMCG",       "FMCG")
)

@Composable
fun FilterChipsRow(activeFilter: String, onFilterChange: (String) -> Unit) {
    Row(
        modifier              = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filterOptions.forEach { option ->
            val selected  = activeFilter == option.value
            val bgColor   by animateColorAsState(
                targetValue = if (selected) Blue500 else PureWhite,
                label       = "chipBg_${option.value}"
            )
            val textColor by animateColorAsState(
                targetValue = if (selected) Color.White else Gray600,
                label       = "chipText_${option.value}"
            )
            Surface(
                onClick  = { onFilterChange(option.value) },
                shape    = RoundedCornerShape(20.dp),
                color    = bgColor,
                modifier = Modifier.border(
                    width = 1.dp,
                    color = if (selected) Blue500 else Gray200,
                    shape = RoundedCornerShape(20.dp)
                )
            ) {
                Text(
                    text       = option.label,
                    modifier   = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                    fontSize   = 12.sp,
                    color      = textColor,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}


@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value         = query,
        onValueChange = onQueryChange,
        placeholder   = {
            Text("Search companies, tags...", color = Gray600.copy(alpha = 0.6f), fontSize = 14.sp)
        },
        modifier    = Modifier.fillMaxWidth(),
        shape       = RoundedCornerShape(14.dp),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Blue500) },
        singleLine  = true,
        colors      = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = PureWhite,
            focusedContainerColor   = PureWhite,
            unfocusedBorderColor    = Gray200,
            focusedBorderColor      = Blue500
        )
    )
}


@Composable
fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Gray600.copy(alpha = 0.6f),
        letterSpacing = 1.2.sp,
        modifier = Modifier.padding(vertical = 10.dp)
    )
}