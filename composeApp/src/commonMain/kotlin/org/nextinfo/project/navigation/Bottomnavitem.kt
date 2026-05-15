package org.nextinfo.project.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int = 0
) {
    data object Home : BottomNavItem(
        route = "home",
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    data object Discover : BottomNavItem(
        route = "discover",
        title = "Discover",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search
    )

    data object Subscriptions : BottomNavItem(
        route = "subscriptions",
        title = "Subscriptions",
        selectedIcon = Icons.Filled.Notifications,
        unselectedIcon = Icons.Outlined.Notifications,
        badgeCount = 3
    )

    data object Tools : BottomNavItem(
        route = "tools",
        title = "Tools",
        selectedIcon = Icons.Filled.Build,
        unselectedIcon = Icons.Outlined.Build
    )

    data object Profile : BottomNavItem(
        route = "org/nextinfo/project/profile",
        title = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )

    companion object {
        /** Ordered list that drives the navigation bar. */
        val items: List<BottomNavItem> by lazy {
            listOf(Home, Discover, Subscriptions, Tools, Profile)
        }
    }
}