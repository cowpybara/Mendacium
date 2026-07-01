package com.example.mendacium.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.mendacium.ui.theme.ImpostorRed
import com.example.mendacium.ui.theme.NightSurface
import com.example.mendacium.ui.theme.OnBackgroundMuted
import com.example.mendacium.ui.theme.playerAvatarColor

private val SeerColor = Color(0xFFD8B4FE)

@Composable
fun SeerRevealScreen(
    investigatedPlayer: Player,
    onContinue: () -> Unit
) {
    val isImpostor = investigatedPlayer.role == Role.Impostor
    val resultColor = if (isImpostor) ImpostorRed else AliveGreen
    val resultLabel = if (isImpostor) "IMPOSTOR" else "INOCENTE"
    val resultIcon = if (isImpostor) "↑" else "✓"
    val flavor = if (isImpostor) {
        "«Su sombra pesa más que la verdad. El velo de ${investigatedPlayer.name} miente.»"
    } else {
        "«Las manos de ${investigatedPlayer.name} están limpias. Por ahora.»"
    }

    val avatarColor = playerAvatarColor(investigatedPlayer.avatarColorIndex)

    Scaffold(
        containerColor = NightSurface,
        topBar = { AccessTopBar(phase = "NOCHE", badge = "REVELACIÓN") }
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
                text = "VISIÓN DE LA VIDENTE",
                color = SeerColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .size(88.dp)
                    .background(avatarColor.copy(alpha = 0.15f), CircleShape)
                    .border(2.dp, avatarColor.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = investigatedPlayer.name.first().uppercaseChar().toString(),
                    color = avatarColor,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = investigatedPlayer.name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(resultColor.copy(alpha = 0.15f))
                    .border(1.dp, resultColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "$resultIcon  $resultLabel",
                    color = resultColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = flavor,
                color = OnBackgroundMuted,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Recuerda: nadie más sabe lo que has visto. Comparte con cautela.",
                color = OnBackgroundMuted.copy(alpha = 0.6f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SeerColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "GUARDAR Y CONTINUAR",
                    color = Color(0xFF0D0B14),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
