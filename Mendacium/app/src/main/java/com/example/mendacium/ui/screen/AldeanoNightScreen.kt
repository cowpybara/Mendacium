package com.example.mendacium.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.model.Player
import com.example.mendacium.ui.theme.DarkBackground
import com.example.mendacium.ui.theme.OnBackgroundMuted
import com.example.mendacium.ui.theme.PurpleAccent
import com.example.mendacium.ui.theme.PurpleSurface

@Composable
fun AldeanoNightScreen(
    allPlayers: List<Player>,
    onConfirmGuess: () -> Unit
) {
    var sospechosoSeleccionado by remember { mutableStateOf<Player?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TURNO DEL ALDEANO",
            color = PurpleAccent,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Manten el misterio.\nSelecciona a tu principal sospechoso para engañar a los demas. Esta eleccion es secreta y no afecta la partida.",
            color = OnBackgroundMuted,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        //lista de jugadores para elegir
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(allPlayers.filter { it.isAlive }) { player ->
                val isSelected = sospechosoSeleccionado == player

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) PurpleAccent.copy(alpha = 0.3f) else PurpleSurface)
                        .clickable { sospechosoSeleccionado = player }
                        .padding(16.dp)
                ) {
                    Text(
                        text = player.name,
                        color = if (isSelected) Color.White else OnBackgroundMuted,
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //boton de confirmar (solo se activa si eligieron a alguien)
        Button(
            onClick = onConfirmGuess,
            enabled = sospechosoSeleccionado != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PurpleAccent,
                disabledContainerColor = PurpleSurface
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "CONFIRMAR SOSPECHA",
                color = if (sospechosoSeleccionado != null) Color.White else Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}