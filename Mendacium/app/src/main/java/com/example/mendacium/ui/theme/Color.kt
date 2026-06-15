package com.example.mendacium.ui.theme

import androidx.compose.ui.graphics.Color

val GreyLavender = Color(0xFFA09DB0)
val DarkBackground = Color(0xFF0D0B14)
val PurpleAccent = Color(0xFFA855F7)
val PurpleSurface = Color(0xFF2A1546)

val CardBackground = Color(0xFF161224)
val CardBorder = Color(0xFF2C283B)
val ImpostorRed = Color(0xFFEF4444)
val ImpostorDarkRed = Color(0xFF8B0000)
val ImpostorDarkRedDisabled = Color(0xFF441818)
val NightSurface = Color(0xFF06141D)
val OnBackgroundMuted = Color(0xFFA09DB0)
val AliveGreen = Color(0xFF4ADE80)

enum class AvatarColor(val color: Color) {
    Violet(Color(0xFF7C3AED)),
    Blue(Color(0xFF2563EB)),
    Emerald(Color(0xFF059669)),
    Amber(Color(0xFFD97706)),
    Red(Color(0xFFDC2626)),
    Cyan(Color(0xFF0891B2)),
    Pink(Color(0xFFDB2777)),
    Lime(Color(0xFF65A30D)),
    Purple(Color(0xFF9333EA)),
    Sky(Color(0xFF0284C7)),
    Green(Color(0xFF16A34A)),
    Yellow(Color(0xFFCA8A04))
}

fun playerAvatarColor(index: Int): Color =
    AvatarColor.entries[index % AvatarColor.entries.size].color
