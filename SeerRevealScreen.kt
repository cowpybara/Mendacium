package com.example.mendacium.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.ui.theme.CardBackground
import com.example.mendacium.ui.theme.DarkBackground
import com.example.mendacium.ui.theme.GreyLavender
import com.example.mendacium.ui.theme.PurpleAccent

@Composable
fun ModeSelectionScreen(
    onSelectLocal: () -> Unit,
    onSelectOnline: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "MENDACIUM",
            color = Color.White,
            fontSize = 34.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "¿Cómo quieres jugar?",
            color = GreyLavender,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        ModeCard(
            emoji = "📱",
            title = "Un teléfono",
            subtitle = "Pasen el dispositivo entre todos. Sin internet.",
            onClick = onSelectLocal
        )

        Spacer(modifier = Modifier.height(16.dp))

        ModeCard(
            emoji = "🌐",
            title = "En línea",
            subtitle = "Cada quien en su propio teléfono. Requiere conexión.",
            onClick = onSelectOnline
        )
    }
}

@Composable
private fun ModeCard(
    emoji: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .border(1.dp, PurpleAccent.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = emoji, fontSize = 40.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = subtitle,
            color = GreyLavender,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}
