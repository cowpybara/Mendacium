package com.example.mendacium.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.model.Player
import com.example.mendacium.ui.component.ConfigurationBottomBar
import com.example.mendacium.ui.component.ConfigurationTopBar
import com.example.mendacium.ui.component.VictimCard
import com.example.mendacium.ui.theme.DarkBackground
import com.example.mendacium.ui.theme.GreyLavender

@Composable
fun ImpostorNightScreen(
    allPlayers: List<Player>,
    currentImpostorName: String,
    onConfirmAttack: (Player) -> Unit
) {
    // FILTRO: Solo vivos y que no sea el impostor mismo[cite: 1]
    val validTargets = remember(allPlayers) {
        allPlayers.filter { it.isAlive && it.name != currentImpostorName }
    }

    var selectedTarget by remember { mutableStateOf<Player?>(null) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = { ConfigurationTopBar() },
        bottomBar = { ConfigurationBottomBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "FASE DE NOCHE",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Elige a quién eliminar esta noche",
                color = GreyLavender,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(validTargets) { victim ->
                    VictimCard(
                        player = victim,
                        isSelected = (victim.name == selectedTarget?.name),
                        onClick = { selectedTarget = victim }
                    )
                }
            }

            Button(
                onClick = { selectedTarget?.let { onConfirmAttack(it) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8B0000),
                    disabledContainerColor = Color(0xFF441818)
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = selectedTarget != null
            ) {
                Text(
                    "CONFIRMAR ATAQUE",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}