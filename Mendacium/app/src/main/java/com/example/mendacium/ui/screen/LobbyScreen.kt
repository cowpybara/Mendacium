package com.example.mendacium.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.model.Player
import com.example.mendacium.model.IconType
import com.example.mendacium.ui.component.NavigationBarSimple
import com.example.mendacium.ui.component.PlayerCard
import com.example.mendacium.ui.component.TopBarSimple
import com.example.mendacium.ui.theme.DarkBackground
import com.example.mendacium.ui.theme.PurpleAccent

@Composable
fun LobbyScreen (
    totalPlayers: Int,
    impostors: Int,
    doctors: Int,
    seers: Int,
    onBack: () -> Unit,
    onStartGame: () -> Unit
) {
    BackHandler {
        onBack()
    }
    // Lista de prueba
    val players = listOf(
        Player("Mateo", "NIVEL 42 • LISTO", true, IconType.LISTO),
        Player("Joshua", "NIVEL 18 • LISTO", false, IconType.NINGUNO),
        Player("Marcos", "NIVEL 05 • CONECTANDO", false, IconType.CONECTANDO),
        Player("Juan", "NIVEL 99 • LISTO", false, IconType.ESTRELLA)
    )

    Scaffold(
        containerColor = DarkBackground,
        topBar = { TopBarSimple(onBackClick = onBack) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Sala de Espera",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF2A1546))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "ESPERANDO JUGADORES... (${players.size}/12)",
                    color = PurpleAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(players) { player ->
                    PlayerCard(player = player)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onStartGame() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(PurpleAccent),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("INICIAR PARTIDA", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}