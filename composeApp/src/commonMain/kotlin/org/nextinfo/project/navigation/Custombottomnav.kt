package org.nextinfo.project.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.nextinfo.project.Blue500
import org.nextinfo.project.Gray600
import org.nextinfo.project.PureWhite


private val NavHeight  = 72.dp
private val FabSize = 56.dp
private val FabGap = 8.dp
private val CurveDepth = 28.dp
private val CurveWidth = 90.dp
private val IndicatorW = 24.dp
private val IndicatorH = 3.dp
private val TabMinW = 56.dp
private const val AnimMs = 320

@Composable
fun CustomBottomNav(selectedItem: BottomNavItem, onItemSelected: (BottomNavItem) -> Unit) {
    val items = BottomNavItem.items
    val fabIndex = items.indexOfFirst { it.route == selectedItem.route }.coerceAtLeast(0)

    val fabFraction = (fabIndex + 0.5f) / items.size
    val fabFractionAnim by animateFloatAsState(
        targetValue = fabFraction,
        animationSpec = tween(AnimMs, easing = FastOutSlowInEasing),
        label = "fabFraction"
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .height(NavHeight + FabSize / 2)
    ) {
        val totalWidth = maxWidth
        val fabCenterX: Dp = totalWidth * fabFractionAnim

        CurvedNavBackground(
            fabCenterX = fabCenterX,
            totalWidth = totalWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(NavHeight)
                .align(Alignment.BottomCenter)
        )

        // Tab row — all tabs rendered uniformly, FAB slot is invisible placeholder
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(NavHeight)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isActive = item.route == selectedItem.route
                val isFabSlot = index == fabIndex

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment  = Alignment.Center
                ) {
                    if (!isFabSlot) {
                        NavTab(
                            item = item,
                            isSelected = isActive,
                            onClick = { onItemSelected(item) }
                        )
                    }
                    // FAB slot is empty - space is occupied by the floating FAB above
                }
            }
        }

        // FAB - positioned at animated center
        FabButton(
            item = items[fabIndex],
            isSelected = true,
            onClick = { /* already selected — no-op or handle re-tap */ },
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = fabCenterX - FabSize / 2, y = 0.dp)
        )
    }
}

@Composable
private fun CurvedNavBackground(fabCenterX: Dp, totalWidth: Dp, modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    val bgColor = PureWhite
    val borderColor = Color(0xFFE5E5EA)

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = with(density) { fabCenterX.toPx() }
        val curveW = with(density) { CurveWidth.toPx() }
        val curveD = with(density) { CurveDepth.toPx() }
        val fabR = with(density) { (FabSize / 2 + FabGap).toPx() }
        val r = with(density) { 24.dp.toPx() }

        val path = Path().apply {
            // Start top-left corner
            moveTo(r, 0f)

            // Straight line to start of notch
            lineTo((cx - curveW / 2f).coerceAtLeast(r), 0f)

            // Left curve of notch
            cubicTo(
                cx - curveW / 2f + 22f, 0f,
                cx - fabR,              curveD,
                cx,                     curveD
            )

            // Right curve of notch
            cubicTo(
                cx + fabR,              curveD,
                cx + curveW / 2f - 22f, 0f,
                (cx + curveW / 2f).coerceAtMost(w - r), 0f
            )

            // Top-right corner
            lineTo(w - r, 0f)
            quadraticBezierTo(w, 0f, w, r)

            // Bottom edge
            lineTo(w, h)
            lineTo(0f, h)
            lineTo(0f, r)

            // Top-left corner
            quadraticBezierTo(0f, 0f, r, 0f)
            close()
        }

        drawPath(path = path, color = bgColor,     style = Fill)
        drawPath(path = path, color = borderColor, style = Stroke(width = 1.dp.toPx()))
    }
}

@Composable
private fun NavTab(item: BottomNavItem, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.92f,
        animationSpec = spring(dampingRatio = 0.55f),
        label = "tabScale"
    )

    Column(
        modifier = modifier
            .scale(scale)
            .widthIn(min = TabMinW)
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .semantics { contentDescription = item.title; role = Role.Tab }
            .padding(horizontal = 6.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
            contentDescription = null,
            tint = if (isSelected) Blue500 else Gray600,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = item.title,
            fontSize = 9.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Blue500 else Gray600,
            maxLines = 1
        )
    }
}

@Composable
private fun FabButton(item: BottomNavItem, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 0.92f else 1f,
        animationSpec = spring(dampingRatio = 0.5f),
        label = "fabScale"
    )

    Box(
        modifier = modifier.scale(scale),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.size(FabSize),
            shape = CircleShape,
            color = Blue500,
            shadowElevation = 10.dp,
            onClick = onClick
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = item.selectedIcon,
                    contentDescription = item.title,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        // Badge
        if (item.badgeCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-2).dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF3D00)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.badgeCount.coerceAtMost(9).toString(),
                    fontSize = 9.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}