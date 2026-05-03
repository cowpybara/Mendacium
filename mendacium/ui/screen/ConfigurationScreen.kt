package com.example.mendacium.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.model.GameConfiguration
import com.example.mendacium.ui.component.ConfigurationBottomBar
import com.example.mendacium.ui.component.ConfigurationTopBar
import com.example.mendacium.ui.component.NumericSelector
import com.example.mendacium.ui.component.RoleConfigCard
import com.example.mendacium.ui.theme.CardBackground
import com.example.mendacium.ui.theme.CardBorder
import com.example.mendacium.ui.theme.DarkBackground
import com.example.mendacium.ui.theme.ImpostorRed
import com.example.mendacium.ui.theme.PurpleAccent

@Composable
fun ConfigurationScreen(
    onNavigateToLobby: (GameConfiguration) -> Unit
) {
    var config by remember { mutableStateOf(GameConfiguration()) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = { ConfigurationTopBar() },
        bottomBar = { ConfigurationBottomBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Configuracion de \nPartida",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
            HorizontalDivider(
                color = Color.Gray,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 24.dp)
                    .width(80.dp),
                thickness = 2.dp
            )
            // Total Players Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardBackground)
                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Group,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "TOTAL DE JUGADORES",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                NumericSelector(
                    value = config.totalPlayers,
                    onDecrease = {
                        if (config.totalPlayers > 4) {
                            config = config.copy(totalPlayers = config.totalPlayers - 1)
                        }
                    },
                    onIncrease = {
                        if (config.totalPlayers < 12) {
                            config = config.copy(totalPlayers = config.totalPlayers + 1)
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Role Grid Row 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                //horizontalArrangement = Arrangement.spacedBy(16.dp)
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                RoleConfigCard(
                    title = "IMPOSTORES",
                    titleColor = ImpostorRed,
                    icon = Icons.Default.Warning,
                    value = config.impostorCount,
                    onDecrease = {
                        if (config.impostorCount > 1) {
                            config = config.copy(impostorCount = config.impostorCount - 1)
                        }
                    },
                    onIncrease = {
                        if (config.impostorCount < 12) {
                            config = config.copy(impostorCount = config.impostorCount + 1)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )

                RoleConfigCard(
                    title = "DOCTORES",
                    titleColor = Color.White,
                    icon = Icons.Default.LocalHospital,
                    value = config.doctorCount,
                    onDecrease = {
                        if (config.doctorCount > 0) {
                            config = config.copy(doctorCount = config.doctorCount - 1)
                        }
                    },
                    onIncrease = {
                        if (config.doctorCount < 12) {
                            config = config.copy(doctorCount = config.doctorCount + 1)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Role Grid Row 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                RoleConfigCard(
                    title = "VIDENTES",
                    titleColor = Color.White,
                    icon = Icons.Default.Visibility,
                    value = config.seerCount,
                    onDecrease = {
                        if (config.seerCount > 0) {
                            config = config.copy(seerCount = config.seerCount - 1)
                        }
                    },
                    onIncrease = {
                        if (config.seerCount < 12) {
                            config = config.copy(seerCount = config.seerCount + 1)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            //Information Note
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Al menos un jugador será aldeano",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            // Primary Button
            Button(
                onClick = { onNavigateToLobby(config) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("REPARTIR ROLES", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}