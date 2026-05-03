package com.example.mendacium.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.model.Player
import com.example.mendacium.model.IconType
import com.example.mendacium.ui.component.PlayerCard

@Composable
fun LobbyScreen(onStartGame: () -> Unit) {
    val players = listOf(
        Player("Vincenzo", "NIVEL 42 • LISTO", isHost = true, iconType = IconType.LISTO),
        Player("Seraphina", "NIVEL 18 • LISTO", iconType = IconType.NINGUNO),
        Player("Kaelith", "NIVEL 05 • CONECTANDO", iconType = IconType.CONECTANDO),
        Player("Nightshade", "NIVEL 99 • LISTO", iconType = IconType.ESTRELLA),
        Player("Oracle", "NIVEL 31 • LISTO", iconType = IconType.NINGUNO)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0B14))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sala de Espera",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp)
        )

        Text(
            text = "ESPERANDO JUGADORES... (5/12)",
            color = Color(0xFFA855F7),
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(players) { player ->
                PlayerCard(player)
            }
        }

        Button(
            onClick = onStartGame,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA855F7)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("INICIAR PARTIDA", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}