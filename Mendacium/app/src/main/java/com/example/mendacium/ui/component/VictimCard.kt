package com.example.mendacium.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.model.Player
import com.example.mendacium.ui.theme.GreyLavender
import com.example.mendacium.ui.theme.PurpleAccent

@Composable
fun VictimCard(
    player: Player,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) PurpleAccent else Color.Transparent
    val titleColor = if (isSelected) PurpleAccent else Color.White
    val subtitleColor = if (isSelected) PurpleAccent else GreyLavender

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF161224))
            .border(
                width = if (isSelected) 1.5.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(Color.Green)
        )
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = player.name,
                color = titleColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = if (isSelected) "Objetivo seleccionado" else player.levelAndStatus,
                color = subtitleColor,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = "Seleccionar victima",
            tint = if (isSelected) PurpleAccent else GreyLavender,
            modifier = Modifier.size(24.dp)
        )
    }
}