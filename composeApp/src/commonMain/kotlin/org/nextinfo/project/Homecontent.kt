package org.nextinfo.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape

@Composable
fun HomeContent(
    paddingValues : PaddingValues,
    subscribed : Set<String>,
    onToggle : (String) -> Unit,
    onCompanyClick : (Company) -> Unit
) {
    val subscribedCompanies = remember(subscribed) {
        tanzaniaCompanies.filter { it.name in subscribed }
    }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        if (subscribedCompanies.isEmpty()) {
            item { HomeEmptyState() }
        } else {
            item {
                HomeGreetingHeader(count = subscribedCompanies.size)
            }

            item { SectionLabel("Your Companies") }

            itemsIndexed(
                items = subscribedCompanies,
                key   = { _, c -> c.name }
            ) { index, company ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) { visible = true }

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(300, delayMillis = index * 60)) +
                            slideInVertically(tween(300, delayMillis = index * 60)) { it / 4 }
                ) {
                    CompanyCard(
                        company = company,
                        isSubscribed = true,
                        onToggle = { onToggle(company.name) },
                        onClick = { onCompanyClick(company) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeGreetingHeader(count: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF1A6BFF), Color(0xFF4D8EFF))
                    ),
                    RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            // Decorative bubble
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-20).dp)
                    .background(Color.White.copy(alpha = 0.06f), CircleShape)
            )

            Column {
                Text(
                    text = "Hello! ${Greeting().greet()} 👋",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Subscribed Companies $count",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Click on any company to view notifications",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun HomeEmptyState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon circle
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Blue100, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Explore,
                    contentDescription = null,
                    tint = Blue500,
                    modifier = Modifier.size(36.dp)
                )
            }

            Text(
                text = "Not subscribed to any company",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Gray900,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Navigate to Discover Choose companies\nand click Subscribe to join.",
                fontSize = 13.sp,
                color = Gray600,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}