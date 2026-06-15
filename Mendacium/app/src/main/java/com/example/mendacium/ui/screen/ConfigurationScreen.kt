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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.model.GameConfiguration
import com.example.mendacium.ui.component.AccessTopBar
import com.example.mendacium.ui.component.PlayerCountSelector
import com.example.mendacium.ui.component.RoleListCard
import com.example.mendacium.ui.theme.CardBackground
import com.example.mendacium.ui.theme.CardBorder
import com.example.mendacium.ui.theme.DarkBackground
import com.example.mendacium.ui.theme.GreyLavender
import com.example.mendacium.ui.theme.ImpostorRed
import com.example.mendacium.ui.theme.PurpleAccent

private val colorAldeano = Color(0xFF60A5FA)
private val colorMedico = Color(0xFF4ADE80)
private val colorVidente = Color(0xFFD8B4FE)

@Composable
fun ConfigurationScreen(
    onNavigateToLobby: (GameConfiguration) -> Unit
) {
    var config by remember { mutableStateOf(GameConfiguration()) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            AccessTopBar(phase = "LOBBY", badge = "ANFITRIÓN")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "CONFIGURACION\nDE PARTIDA",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Italic,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Define la composición y duración de las fases antes de invitar.",
                color = GreyLavender,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            SectionHeader(icon = Icons.Default.Group, label = "TOTAL PARTICIPANTES")

            Spacer(modifier = Modifier.height(10.dp))

            PlayerCountSelector(
                value = config.totalPlayers,
                minValue = GameConfiguration.MIN_TOTAL_PLAYERS,
                maxValue = GameConfiguration.MAX_TOTAL_PLAYERS,
                onDecrease = {
                    val next = config.totalPlayers - 1
                    if (next >= GameConfiguration.MIN_TOTAL_PLAYERS &&
                        next >= config.impostorCount + config.doctorCount + config.seerCount + 1
                    ) {
                        config = config.copy(totalPlayers = next)
                    }
                },
                onIncrease = {
                    if (config.totalPlayers < GameConfiguration.MAX_TOTAL_PLAYERS) {
                        config = config.copy(totalPlayers = config.totalPlayers + 1)
                    }
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            SectionHeader(icon = Icons.Default.Warning, label = "REPARTO DE ROLES", iconTint = ImpostorRed)

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                RoleListCard(
                    name = "Aldeanos",
                    roleColor = colorAldeano,
                    icon = Icons.Default.Person,
                    value = config.villagerCount,
                    enabled = false,
                    onDecrease = {},
                    onIncrease = {}
                )

                RoleListCard(
                    name = "Impostores",
                    roleColor = ImpostorRed,
                    icon = Icons.Default.Warning,
                    value = config.impostorCount,
                    onDecrease = {
                        if (config.impostorCount > GameConfiguration.MIN_IMPOSTOR_COUNT) {
                            config = config.copy(impostorCount = config.impostorCount - 1)
                        }
                    },
                    onIncrease = {
                        if (config.villagerCount > GameConfiguration.MIN_VILLAGER_COUNT) {
                            config = config.copy(impostorCount = config.impostorCount + 1)
                        }
                    }
                )

                RoleListCard(
                    name = "Médico",
                    roleColor = colorMedico,
                    icon = Icons.Default.LocalHospital,
                    value = config.doctorCount,
                    onDecrease = {
                        if (config.doctorCount > 0) {
                            config = config.copy(doctorCount = config.doctorCount - 1)
                        }
                    },
                    onIncrease = {
                        if (config.villagerCount > GameConfiguration.MIN_VILLAGER_COUNT) {
                            config = config.copy(doctorCount = config.doctorCount + 1)
                        }
                    }
                )

                RoleListCard(
                    name = "Vidente",
                    roleColor = colorVidente,
                    icon = Icons.Default.Visibility,
                    value = config.seerCount,
                    onDecrease = {
                        if (config.seerCount > 0) {
                            config = config.copy(seerCount = config.seerCount - 1)
                        }
                    },
                    onIncrease = {
                        if (config.villagerCount > GameConfiguration.MIN_VILLAGER_COUNT) {
                            config = config.copy(seerCount = config.seerCount + 1)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            SectionHeader(icon = Icons.Default.AccessTime, label = "DURACIÓN DE FASES")

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardBackground)
                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Próximamente disponible",
                    color = GreyLavender,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onNavigateToLobby(config) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurpleAccent,
                    disabledContainerColor = PurpleAccent.copy(alpha = 0.35f)
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = config.hasAtLeastOneVillager()
            ) {
                Icon(
                    imageVector = Icons.Default.Groups,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ABRIR SALA DE ESPERA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionHeader(
    icon: ImageVector,
    label: String,
    iconTint: Color = GreyLavender
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            color = GreyLavender,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}
