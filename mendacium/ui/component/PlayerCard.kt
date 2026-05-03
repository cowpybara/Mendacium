package com.example.mendacium.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.model.Player
import com.example.mendacium.model.IconType

@Composable
fun PlayerCard(player: Player) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1A1629))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF2D283E)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray)
            if (player.isHost) {
                Text(
                    text = "ANFITRIÓN",
                    color = Color.White,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(Color(0xFFA855F7), RoundedCornerShape(bottomEnd = 4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(player.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(player.levelAndStatus, color = Color(0xFFA09DB0), fontSize = 12.sp)
        }
        val iconColor = Color(0xFFA855F7)
        when (player.iconType) {
            IconType.LISTO -> Icon(Icons.Default.CheckCircle, "", tint = iconColor)
            IconType.ESTRELLA -> Icon(Icons.Default.Star, "", tint = iconColor)
            IconType.CONECTANDO -> Icon(Icons.Default.Refresh, "", tint = Color.Gray)
            IconType.NINGUNO -> Icon(Icons.Default.AccountCircle, "", tint = Color.Gray)
        }
    }
}