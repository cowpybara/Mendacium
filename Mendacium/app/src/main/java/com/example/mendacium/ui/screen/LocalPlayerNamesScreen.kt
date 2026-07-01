package com.example.mendacium.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.ui.theme.DarkBackground
import com.example.mendacium.ui.theme.OnBackgroundMuted
import com.example.mendacium.ui.theme.PurpleAccent

@Composable
fun LocalPlayerNamesScreen(
    totalPlayers: Int,
    hostName: String, // Recibe el nombre del maje que creó la sala
    onContinuar: (List<String>) -> Unit
) {
    // Crea la lista de nombres. A la primera casilla le pone el hostName de un solo.
    val nombres = remember {
        mutableStateListOf<String>().apply {
            repeat(totalPlayers) { index ->
                add(if (index == 0) hostName else "")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "Nombres de los Jugadores",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp, top = 24.dp)
        )

        Text(
            text = "Pasen el teléfono y escriban sus nombres para la partida local.",
            fontSize = 14.sp,
            color = OnBackgroundMuted,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(totalPlayers) { index ->
                OutlinedTextField(
                    value = nombres[index],
                    onValueChange = { nombres[index] = it },
                    label = { Text(if (index == 0) "Anfitrión" else "Jugador ${index + 1}") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PurpleAccent,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = PurpleAccent,
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    // Evitamos que alguien borre por accidente el nombre del host (opcional, pero ayuda)
                    readOnly = index == 0
                )
            }
        }

        Button(
            onClick = {
                // Rellena con nombres genéricos por si a algún maje se le olvidó poner su nombre
                val nombresFinales = nombres.mapIndexed { i, n ->
                    if (n.isBlank()) "Jugador ${i + 1}" else n.trim()
                }
                onContinuar(nombresFinales)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "CONTINUAR AL LOBBY",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}