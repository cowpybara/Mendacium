package com.example.mendacium.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.ui.component.TopBarSimple
import com.example.mendacium.ui.theme.DarkBackground
import com.example.mendacium.ui.theme.OnBackgroundMuted
import com.example.mendacium.ui.theme.PurpleAccent

@Composable
fun NightSummaryScreen(
    eliminatedPlayerName: String?,
    onReturnToStart: () -> Unit
) {
    val message = if (eliminatedPlayerName == null) {
        "Nadie fue eliminado esta noche."
    } else {
        "$eliminatedPlayerName fue eliminado durante la noche."
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = { TopBarSimple() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "LA NOCHE TERMINA",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161224)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = message,
                    color = OnBackgroundMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onReturnToStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("VOLVER AL INICIO", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
