package com.example.mendacium.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
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
import com.example.mendacium.model.IconType
import com.example.mendacium.ui.theme.GreyLavender
import com.example.mendacium.ui.theme.PurpleAccent

@Composable
fun PlayerCard (player: Player) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF161224))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar y etiqueta anfitrion
        Box(modifier = Modifier.size(56.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF2C283B))
            )

            if (player.isHost) {
                Text(
                    text = "ANFITRION",
                    color = Color(0xFF0D0B14),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color(0xFFA855F7), RoundedCornerShape(topStart = 8.dp, bottomEnd = 4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .align(Alignment.TopStart)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = player.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = player.levelAndStatus,
                color = Color(0xFFA09DB0),
                fontSize = 12.sp
            )
        }

        val iconModifier = Modifier.size(24.dp)
        when (player.iconType) {
            IconType.LISTO -> Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = PurpleAccent,
                modifier = iconModifier
            )
            IconType.ESTRELLA -> Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = PurpleAccent,
                modifier = iconModifier
            )
            IconType.CONECTANDO -> Icon(
                Icons.Default.Refresh,
                contentDescription = null,
                tint = GreyLavender,
                modifier = iconModifier
            )
            IconType.NINGUNO -> Icon(
                Icons.Default.AccountCircle,
                contentDescription = null,
                tint = GreyLavender,
                modifier = iconModifier
            )
        }
    }
}