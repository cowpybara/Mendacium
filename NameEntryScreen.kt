package com.example.mendacium.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.model.Player
import com.example.mendacium.ui.component.AccessTopBar
import com.example.mendacium.ui.theme.CardBackground
import com.example.mendacium.ui.theme.DarkBackground
import com.example.mendacium.ui.theme.OnBackgroundMuted
import com.example.mendacium.ui.theme.PurpleAccent
import com.example.mendacium.ui.theme.playerAvatarColor
import kotlinx.coroutines.delay

private const val DISCUSSION_SECONDS = 120

@Composable
fun DiscussionScreen(
    players: List<Player>,
    dayNumber: Int,
    onProceedToVoting: () -> Unit
) {
    val alivePlayers = remember(players) { players.filter { it.isAlive } }
    var timeLeft by remember { mutableIntStateOf(DISCUSSION_SECONDS) }

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val timerText = "%02d:%02d".format(minutes, seconds)
    val progress = timeLeft.toFloat() / DISCUSSION_SECONDS
    val timerColor = if (timeLeft <= 30) Color(0xFFF59E0B) else PurpleAccent

    Scaffold(
        containerColor = DarkBackground,
        topBar = { AccessTopBar(phase = "DÍA · DISCUSIÓN", badge = "DÍA $dayNumber") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Timer card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardBackground)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = timerText,
                        color = timerColor,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    // Barra de progreso manual (evita dependencia de API experimental)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(PurpleAccent.copy(alpha = 0.2f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .fillMaxHeight()
                                .background(timerColor)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "VIVOS",
                    color = OnBackgroundMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${alivePlayers.size}/${players.size}",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Chips de jugadores vivos en fila horizontal deslizable
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(alivePlayers) { player ->
                    val color = playerAvatarColor(player.avatarColorIndex)
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(color.copy(alpha = 0.15f), CircleShape)
                            .border(1.dp, color.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = player.name.first().uppercaseChar().toString(),
                            color = color,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Discusión libre. Defiéndete, acusa y deduce.\nEl tiempo corre.",
                color = OnBackgroundMuted,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onProceedToVoting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "PASAR A VOTACIÓN",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
