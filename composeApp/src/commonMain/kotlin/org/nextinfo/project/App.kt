package org.nextinfo.project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.nextinfo.project.navigation.BottomNavItem
import org.nextinfo.project.navigation.CustomBottomNav

@Composable
fun App() {
    var selectedNav: BottomNavItem by remember { mutableStateOf(BottomNavItem.Home) }
    var selectedCompany: Company?  by remember { mutableStateOf(null) }
    val subscribed = remember { mutableStateSetOf<String>() }

    NextInfoTheme {
        Scaffold(
            containerColor = PageBG,
            bottomBar = {
                if (selectedCompany == null) {
                    CustomBottomNav(
                        selectedItem = selectedNav,
                        onItemSelected = { selectedNav = it }
                    )
                }
            },
            topBar = {
                AppTopBar()
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (selectedNav) {
                    BottomNavItem.Discover -> DiscoverContent(
                        paddingValues = PaddingValues(0.dp),
                        subscribed = subscribed,
                        onToggle = { name ->
                            if (name in subscribed) subscribed.remove(name)
                            else subscribed.add(name)
                        },
                        onCompanyClick = { company -> selectedCompany = company }
                    )

                    BottomNavItem.Home -> HomeContent(
                        paddingValues = PaddingValues(0.dp),
                        subscribed = subscribed,
                        onToggle = { name ->
                            if (name in subscribed) subscribed.remove(name)
                            else subscribed.add(name)
                        },
                        onCompanyClick = { company -> selectedCompany = company }
                    )

                    else -> PlaceholderScreen(
                        paddingValues = PaddingValues(0.dp),
                        label = selectedNav.title
                    )
                }

                if (selectedCompany != null) {
                    CompanyProfileScreen(
                        company = selectedCompany!!,
                        onBack = { selectedCompany = null }
                    )
                }
            }
        }
    }
}

@Composable
private fun AppTopBar() {
    Surface(shadowElevation = 2.dp, color = PureWhite) {
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "NextInfo",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = Blue500,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PlaceholderScreen(paddingValues: PaddingValues, label: String) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$label coming soon",
            fontSize = 16.sp,
            color = Gray600,
            fontWeight = FontWeight.Medium
        )
    }
}