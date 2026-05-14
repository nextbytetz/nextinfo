package org.nextinfo.project.auth

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val AuthBlue = Color(0xFF1A6BFF)
val AuthBlueDark = Color(0xFF1355D6)
val AuthBlueLight = Color(0xFF4D8EFF)
val AuthBluePale = Color(0xFFEBF1FF)
val AuthSurface = Color(0xFFFFFFFF)
val AuthBackground = Color(0xFFF4F7FF)
val AuthTextPrimary = Color(0xFF1C1C1E)
val AuthTextSecond = Color(0xFF6B7280)
val AuthBorder = Color(0xFFE4E7EC)
val AuthError = Color(0xFFEF4444)

@Composable
fun AuthScaffold(
    scrollState : ScrollState = rememberScrollState(),
    content : @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthBackground)
    ) {
        // Decorative gradient blobs
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(x = (-60).dp, y = (-60).dp)
                .background(
                    Brush.radialGradient(
                        listOf(AuthBlue.copy(alpha = 0.12f), Color.Transparent)
                    ),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 60.dp, y = 60.dp)
                .background(
                    Brush.radialGradient(
                        listOf(AuthBlueLight.copy(alpha = 0.10f), Color.Transparent)
                    ),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            content = content
        )
    }
}