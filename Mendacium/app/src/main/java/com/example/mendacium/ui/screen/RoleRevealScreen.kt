package com.example.mendacium.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.model.Role
import com.example.mendacium.ui.theme.NightSurface
import com.example.mendacium.ui.theme.OnBackgroundMuted

@Composable
fun RoleRevealScreen(
    playerName: String,
    role: Role,
    onUnderstand: () -> Unit
) {
    val icon = when (role) {
        Role.Vidente -> Icons.Default.Visibility
        Role.Impostor -> Icons.Default.Warning
        Role.Doctor -> Icons.Default.Favorite
        Role.Aldeano -> Icons.Default.Person
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NightSurface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = playerName,
            color = OnBackgroundMuted,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "EL VELO SE LEVANTA",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .size(200.dp)
                .background(role.color.copy(alpha = 0.1f), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = role.color
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = role.name,
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0B1E2B)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = role.description,
                color = OnBackgroundMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onUnderstand,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = role.color),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("ENTENDIDO", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
