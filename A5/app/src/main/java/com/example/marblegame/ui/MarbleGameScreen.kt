package com.example.marblegame.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.marblegame.viewmodel.MarbleViewModel
import kotlin.math.roundToInt

/**
 * Main screen rendering the Marble Game with real-time sensor motion and physics.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarbleGameScreen(
    viewModel: MarbleViewModel,
    modifier: Modifier = Modifier
) {
    val marbleState by viewModel.marbleState.collectAsState()
    val marbleSizeDp = 48.dp

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Marble Motion Game",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF2E7D32)) // Felt green game table background
        ) {
            // HUD Information Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xDD000000)
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Sensors,
                                contentDescription = null,
                                tint = Color(0xFF81D4FA)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = marbleState.sensorName,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { viewModel.resetMarble() },
                            modifier = Modifier.height(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset Marble",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Center", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Tilt X: ${formatFloat(marbleState.tiltX)} | Y: ${formatFloat(marbleState.tiltY)}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFFB0BEC5)
                        )
                        Text(
                            text = "Vel X: ${marbleState.vx.roundToInt()} | Y: ${marbleState.vy.roundToInt()}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFFB0BEC5)
                        )
                    }
                }
            }

            // Playable Playfield using BoxWithConstraints
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .border(4.dp, Color(0xFF1B5E20), RoundedCornerShape(16.dp))
                    .background(Color(0xFF388E3C), RoundedCornerShape(16.dp))
            ) {
                val density = LocalDensity.current

                val widthPx = with(density) { maxWidth.toPx() }
                val heightPx = with(density) { maxHeight.toPx() }
                val marbleSizePx = with(density) { marbleSizeDp.toPx() }

                // Update screen bounds in ViewModel whenever constraints or density change
                LaunchedEffect(maxWidth, maxHeight, density) {
                    viewModel.updateScreenBounds(widthPx, heightPx, marbleSizePx)
                }

                // Convert pixel coordinates to Dp for offset modifier
                val xDp = with(density) { marbleState.xPx.toDp() }
                val yDp = with(density) { marbleState.yPx.toDp() }

                // Render Marble Ball
                Box(
                    modifier = Modifier
                        .offset(xDp, yDp)
                        .size(marbleSizeDp)
                        .shadow(elevation = 8.dp, shape = CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFE0F7FA), // Highlight
                                    Color(0xFF29B6F6), // Base blue
                                    Color(0xFF0277BD)  // Shadow blue
                                )
                            ),
                            shape = CircleShape
                        )
                        .border(1.5.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                )
            }
        }
    }
}

private fun formatFloat(value: Float): String {
    return String.format("%.1f", value)
}
