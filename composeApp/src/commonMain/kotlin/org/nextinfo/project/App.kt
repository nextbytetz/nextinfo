package org.nextinfo.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nextinfo.composeapp.generated.resources.Res
import nextinfo.composeapp.generated.resources.logo
import org.nextinfo.project.auth.AuthHost
import org.nextinfo.project.navigation.BottomNavItem
import org.nextinfo.project.navigation.CustomBottomNav
import org.jetbrains.compose.resources.painterResource
import org.nextinfo.project.profile.ProfileContent
import org.nextinfo.project.settings.ToolsContent


@Composable
fun App() {
    var isAuthenticated by remember { mutableStateOf(false) }
    NextInfoTheme {
        if (!isAuthenticated) {
            AuthHost(onAuthSuccess = { isAuthenticated = true })
        } else {
            MainApp()
        }
    }
}


@Composable
private fun MainApp() {
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
                    BottomNavItem.Profile -> ProfileContent(
                        paddingValues = PaddingValues(0.dp),
                    )

                    BottomNavItem.Tools -> ToolsContent(
                        paddingValues = PaddingValues(0.dp),
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
    Surface(
        shadowElevation = 3.dp,
        color = PureWhite
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "NextInfo Logo",
                modifier = Modifier
                    .height(45.dp)
                    .wrapContentWidth()
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .height(24.dp)
                    .width(1.dp)
                    .background(Color.LightGray.copy(alpha = 0.5f))
            )

            // Slogan
            Column {
                Text(
                    text = "Kua Wakwanza,",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue500,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Usipitwe na taarifa yoyote.",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray,
                    letterSpacing = 4.sp
                )
            }
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