package com.example.mendacium.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.model.Player
import com.example.mendacium.model.Role
import com.example.mendacium.ui.component.AccessTopBar
import com.example.mendacium.ui.theme.AliveGreen
import com.example.mendacium.ui.theme.CardBackground
import com.example.mendacium.ui.theme.DarkBackground
import com.example.mendacium.ui.theme.ImpostorRed
import com.example.mendacium.ui.theme.OnBackgroundMuted
import com.example.mendacium.ui.theme.PurpleAccent
import com.example.mendacium.ui.theme.playerAvatarColor

@Composable
fun VerdictScreen(
    eliminatedPlayer: Player?,
    allPlayers: List<Player>,
    dayNumber: Int,
    isGameOver: Boolean,
    townsfolkWon: Boolean,
    onNextNight: () -> Unit,
    onNewGame: () -> Unit
) {
    val aliveCount = allPlayers.count { it.isAlive }
    val aliveImpostors = allPlayers.count { it.isAlive && it.role == Role.Impostor }

    val isImpostorEliminated = eliminatedPlayer?.role == Role.Impostor
    val accentColor = when {
        isGameOver && townsfolkWon -> AliveGreen
        isGameOver && !townsfolkWon -> ImpostorRed
        isImpostorEliminated -> AliveGreen
        else -> ImpostorRed
    }
    val verdictText = when {
        eliminatedPlayer == null -> "El pueblo se abstuvo."
        isImpostorEliminated -> "El pueblo acertó."
        else -> "El pueblo falló."
    }
    val gameOverTitle = when {
        isGameOver && townsfolkWon -> "¡EL PUEBLO GANA!"
        isGameOver && !townsfolkWon -> "¡LOS IMPOSTORES GANAN!"
        else -> "VEREDICTO FINAL"
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            AccessTopBar(
                phase = if (isGameOver) "FIN DE PARTIDA" else "DÍA · VEREDICTO",
                badge = "DÍA $dayNumber"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = gameOverTitle,
                color = accentColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (eliminatedPlayer != null) {
                val avatarColor = playerAvatarColor(eliminatedPlayer.avatarColorIndex)
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .background(avatarColor.copy(alpha = 0.15f), CircleShape)
                        .border(2.dp, avatarColor.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = eliminatedPlayer.name.first().uppercaseChar().toString(),
                        color = avatarColor,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "@${eliminatedPlayer.name}",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(eliminatedPlayer.role.color.copy(alpha = 0.15f))
                        .border(1.dp, eliminatedPlayer.role.color.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = "ERA ${eliminatedPlayer.role.name}",
                        color = eliminatedPlayer.role.color,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            } else {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .background(OnBackgroundMuted.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🤷", fontSize = 40.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            Text(
                text = verdictText,
                color = accentColor,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )

            if (isGameOver) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (townsfolkWon) "Todos los impostores han sido eliminados." else "Los impostores dominan el pueblo.",
                    color = OnBackgroundMuted,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Stats
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardBackground)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(label = "VIVOS", value = "$aliveCount/${allPlayers.size}")
                    StatItem(label = "IMPOSTORES", value = "$aliveImpostors")
                    StatItem(label = "DÍA", value = "$dayNumber")
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            if (isGameOver) {
                Button(
                    onClick = onNewGame,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("NUEVA PARTIDA", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            } else {
                Button(
                    onClick = onNextNight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("🌙  CAE LA NOCHE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = OnBackgroundMuted,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}
