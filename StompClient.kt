package com.example.mendacium.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.model.Player
import com.example.mendacium.ui.component.AccessTopBar
import com.example.mendacium.ui.theme.CardBackground
import com.example.mendacium.ui.theme.CardBorder
import com.example.mendacium.ui.theme.DarkBackground
import com.example.mendacium.ui.theme.ImpostorRed
import com.example.mendacium.ui.theme.OnBackgroundMuted
import com.example.mendacium.ui.theme.PurpleAccent
import com.example.mendacium.ui.theme.playerAvatarColor

@Composable
fun VotingScreen(
    players: List<Player>,
    dayNumber: Int,
    eliminatedNight: String?,
    onConfirmVote: (Player?) -> Unit
) {
    val alivePlayers = remember(players) { players.filter { it.isAlive } }
    var votedPlayer by remember { mutableStateOf<Player?>(null) }
    var abstained by remember { mutableStateOf(false) }
    var yaConfirme by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = { AccessTopBar(phase = "DÍA · VOTACIÓN", badge = "DÍA $dayNumber") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            if (eliminatedNight != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(ImpostorRed.copy(alpha = 0.08f))
                        .border(1.dp, ImpostorRed.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "El jugador $eliminatedNight ha sido eliminado. Su cuerpo ha sido hallado al amanecer.",
                        color = OnBackgroundMuted,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = "Votación Activa",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Selecciona a quién quieres linchar.",
                color = OnBackgroundMuted,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(alivePlayers) { player ->
                    VotePlayerCard(
                        player = player,
                        isSelected = player.name == votedPlayer?.name,
                        onClick = {
                            votedPlayer = player
                            abstained = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (yaConfirme) {
                // En línea: tras votar se espera a que voten los demás jugadores
                Text(
                    text = "✓ Voto registrado",
                    color = PurpleAccent,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Esperando a que voten los demás...",
                    color = OnBackgroundMuted,
                    fontSize = 13.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                TextButton(
                    onClick = {
                        abstained = true
                        votedPlayer = null
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = if (abstained) "✓ ABSTENIÉNDOSE" else "× ABSTENERSE",
                        color = if (abstained) PurpleAccent else OnBackgroundMuted,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        yaConfirme = true
                        onConfirmVote(if (abstained) null else votedPlayer)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PurpleAccent,
                        disabledContainerColor = PurpleAccent.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = votedPlayer != null || abstained
                ) {
                    Text(
                        "CONFIRMAR RESULTADO",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun VotePlayerCard(
    player: Player,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val avatarColor = playerAvatarColor(player.avatarColorIndex)
    val borderColor = if (isSelected) PurpleAccent else CardBorder

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardBackground)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(avatarColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                .border(1.dp, avatarColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = player.name.first().uppercaseChar().toString(),
                color = avatarColor,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = player.name,
            color = if (isSelected) Color.White else OnBackgroundMuted,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            tint = if (isSelected) PurpleAccent else OnBackgroundMuted.copy(alpha = 0.4f),
            modifier = Modifier.size(18.dp)
        )
    }
}
