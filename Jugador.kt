package com.example.mendacium.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.example.mendacium.model.IconType
import com.example.mendacium.model.Player
import com.example.mendacium.ui.theme.CardBackground
import com.example.mendacium.ui.theme.GreyLavender
import com.example.mendacium.ui.theme.OnBackgroundMuted
import com.example.mendacium.ui.theme.PurpleAccent
import com.example.mendacium.ui.theme.playerAvatarColor

@Composable
fun PlayerCard(player: Player) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardBackground)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val avatarColor = playerAvatarColor(player.avatarColorIndex)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(avatarColor.copy(alpha = 0.15f))
                .border(1.dp, avatarColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = player.name.first().uppercaseChar().toString(),
                color = avatarColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            if (player.isHost) {
                Text(
                    text = "★",
                    color = Color(0xFF0D0B14),
                    fontSize = 6.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(avatarColor, RoundedCornerShape(topStart = 8.dp, bottomEnd = 4.dp))
                        .padding(horizontal = 3.dp, vertical = 1.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = player.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = player.levelAndStatus,
                color = OnBackgroundMuted,
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
