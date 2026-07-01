package com.example.mendacium.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.model.Player
import com.example.mendacium.ui.component.AccessTopBar
import com.example.mendacium.ui.component.VictimCard
import com.example.mendacium.ui.theme.ImpostorDarkRed
import com.example.mendacium.ui.theme.ImpostorDarkRedDisabled
import com.example.mendacium.ui.theme.ImpostorRed
import com.example.mendacium.ui.theme.NightSurface
import com.example.mendacium.ui.theme.OnBackgroundMuted

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
        containerColor = NightSurface,
        topBar = {
            AccessTopBar(phase = "NOCHE", badge = "IMPOSTOR")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Phase badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ImpostorRed.copy(alpha = 0.15f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = ImpostorRed,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "  FASE DE NOCHE · IMPOSTOR",
                        color = ImpostorRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "$attackingSideLabel elige a quién eliminar esta noche.",
                color = OnBackgroundMuted,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(validTargets) { victim ->
                    VictimCard(
                        player = victim,
                        isSelected = victim.name == selectedTarget?.name,
                        onClick = { selectedTarget = victim }
                    )
                }
            }

            // Flavor quote
            if (selectedTarget != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "«Que tu silencio sea su epitafio»",
                    color = ImpostorRed.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { selectedTarget?.let(onConfirmAttack) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ImpostorDarkRed,
                    disabledContainerColor = ImpostorDarkRedDisabled
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = selectedTarget != null
            ) {
                Text(
                    "ELIMINAR",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
