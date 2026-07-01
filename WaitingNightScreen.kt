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
import androidx.compose.material.icons.filled.Visibility
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
import com.example.mendacium.ui.theme.NightSurface
import com.example.mendacium.ui.theme.OnBackgroundMuted

private val SeerColor = Color(0xFFD8B4FE)

@Composable
fun SeerNightScreen(
    allPlayers: List<Player>,
    seerName: String,
    onConfirmInvestigation: (Player) -> Unit
) {
    val targets = remember(allPlayers) {
        allPlayers.filter { it.isAlive && it.name != seerName }
    }
    var selectedTarget by remember { mutableStateOf<Player?>(null) }

    Scaffold(
        containerColor = NightSurface,
        topBar = { AccessTopBar(phase = "NOCHE", badge = "VIDENTE") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(SeerColor.copy(alpha = 0.12f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = SeerColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "  FASE DE NOCHE · VIDENTE",
                        color = SeerColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Dile a un jugador para investigar su identidad.",
                color = OnBackgroundMuted,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(targets) { player ->
                    VictimCard(
                        player = player,
                        isSelected = player.name == selectedTarget?.name,
                        onClick = { selectedTarget = player }
                    )
                }
            }

            if (selectedTarget != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "«La verdad se oculta tras el velo»",
                    color = SeerColor.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { selectedTarget?.let(onConfirmInvestigation) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SeerColor,
                    disabledContainerColor = SeerColor.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = selectedTarget != null
            ) {
                Text(
                    "INVESTIGAR",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF0D0B14)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
