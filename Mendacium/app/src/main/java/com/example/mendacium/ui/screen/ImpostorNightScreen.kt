package com.example.mendacium.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    excludedPlayerNames: Set<String>,
    attackingSideLabel: String,
    onConfirmAttack: (Player) -> Unit
) {
    val validTargets = remember(allPlayers, excludedPlayerNames) {
        allPlayers.filter { it.isAlive && it.name !in excludedPlayerNames }
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
                text = "$attackingSideLabel eligen a quien eliminar esta noche",
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
                        isSelected = victim.name == selectedTarget?.name,
                        onClick = { selectedTarget = victim }
                    )
                }
            }

            Button(
                onClick = { selectedTarget?.let(onConfirmAttack) },
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
