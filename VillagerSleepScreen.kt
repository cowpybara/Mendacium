package com.example.mendacium.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.ui.theme.DarkBackground
import com.example.mendacium.ui.theme.OnBackgroundMuted
import com.example.mendacium.ui.theme.PurpleAccent
import com.example.mendacium.ui.theme.PurpleSurface
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PassDeviceScreen(
    playerName: String,
    turnNumber: Int,
    totalPlayers: Int,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "PASA EL DISPOSITIVO",
            color = PurpleAccent,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .background(PurpleSurface, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🌙", fontSize = 52.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(PurpleSurface)
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = "TURNO $turnNumber · NOCHE",
                color = OnBackgroundMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Entrega el teléfono a",
            color = OnBackgroundMuted,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = playerName,
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Mantén la pantalla oculta de los demás.",
            color = OnBackgroundMuted,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        HoldToRevealButton(playerName = playerName, onComplete = onContinue)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "MANTÉN PULSADO PARA REVELAR",
            color = OnBackgroundMuted,
            fontSize = 11.sp,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun HoldToRevealButton(
    playerName: String,
    onComplete: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var holdProgress by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(PurpleAccent)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        var completed = false
                        val job = scope.launch {
                            val totalSteps = 100
                            repeat(totalSteps + 1) { step ->
                                holdProgress = step / totalSteps.toFloat()
                                if (step == totalSteps) {
                                    completed = true
                                    onComplete()
                                    return@launch
                                }
                                delay(20L)
                            }
                        }
                        tryAwaitRelease()
                        job.cancel()
                        if (!completed) holdProgress = 0f
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(holdProgress)
                .align(Alignment.CenterStart)
                .background(Color.White.copy(alpha = 0.25f))
        )
        Text(
            text = "SOY ${playerName.uppercase()} · CONTINUAR",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}
