package com.example.mendacium.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.ui.component.AccessTopBar
import com.example.mendacium.ui.theme.CardBackground
import com.example.mendacium.ui.theme.CardBorder
import com.example.mendacium.ui.theme.DarkBackground
import com.example.mendacium.ui.theme.GreyLavender
import com.example.mendacium.ui.theme.PurpleAccent

private const val CODE_LENGTH = 5

@Composable
fun JoinWithCodeScreen(
    playerName: String,
    onBack: () -> Unit,
    onEnterRoom: (code: String) -> Unit
) {
    var code by remember { mutableStateOf("") }
    var isLetterMode by remember { mutableStateOf(false) }

    val isCodeComplete = code.length == CODE_LENGTH

    fun onKeyPress(char: Char) {
        if (code.length < CODE_LENGTH) code += char.uppercaseChar()
    }

    fun onBackspace() {
        if (code.isNotEmpty()) code = code.dropLast(1)
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            AccessTopBar(
                badge = playerName.ifEmpty { "ANÓNIMO" },
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "ACCESO A SALA",
                color = PurpleAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Introduce el código",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "El anfitrión te facilitará una clave de 5 caracteres.\nCada sala es privada y se sella al iniciar.",
                color = GreyLavender,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Código: 5 cajas
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(CODE_LENGTH) { index ->
                    val char = code.getOrNull(index)
                    val isActive = index == code.length
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CardBackground)
                            .border(
                                width = if (isActive) 2.dp else 1.dp,
                                color = when {
                                    isActive -> PurpleAccent
                                    char != null -> PurpleAccent.copy(alpha = 0.5f)
                                    else -> CardBorder
                                },
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char?.toString() ?: "",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nota informativa
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CardBackground)
                    .border(1.dp, CardBorder, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = GreyLavender,
                    modifier = Modifier.size(14.dp).padding(top = 1.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "¿No tienes código? Pídelo al anfitrión.",
                    color = GreyLavender,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Teclado personalizado
            if (!isLetterMode) {
                NumberKeyboard(
                    onNumber = { onKeyPress(it) },
                    onBackspace = { onBackspace() },
                    onToggleLetters = { isLetterMode = true }
                )
            } else {
                LetterKeyboard(
                    onLetter = { onKeyPress(it) },
                    onBackspace = { onBackspace() },
                    onToggleNumbers = { isLetterMode = false }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onEnterRoom(code) },
                enabled = isCodeComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurpleAccent,
                    disabledContainerColor = PurpleAccent.copy(alpha = 0.35f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "ENTRAR EN LA SALA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun NumberKeyboard(
    onNumber: (Char) -> Unit,
    onBackspace: () -> Unit,
    onToggleLetters: () -> Unit
) {
    val rows = listOf(
        listOf('1', '2', '3'),
        listOf('4', '5', '6'),
        listOf('7', '8', '9')
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { digit ->
                    KeyButton(
                        label = digit.toString(),
                        modifier = Modifier.weight(1f),
                        onClick = { onNumber(digit) }
                    )
                }
            }
        }
        // Fila inferior: ABC | 0 | ⌫
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KeyButton(
                label = "ABC",
                modifier = Modifier.weight(1f),
                labelSize = 13.sp,
                onClick = onToggleLetters
            )
            KeyButton(
                label = "0",
                modifier = Modifier.weight(1f),
                onClick = { onNumber('0') }
            )
            BackspaceKey(modifier = Modifier.weight(1f), onClick = onBackspace)
        }
    }
}

@Composable
private fun LetterKeyboard(
    onLetter: (Char) -> Unit,
    onBackspace: () -> Unit,
    onToggleNumbers: () -> Unit
) {
    val rows = listOf(
        "QWERTYUIOP".toList(),
        "ASDFGHJKL".toList(),
        "ZXCVBNM".toList()
    )

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                row.forEach { letter ->
                    KeyButton(
                        label = letter.toString(),
                        modifier = Modifier.weight(1f),
                        labelSize = 14.sp,
                        onClick = { onLetter(letter) }
                    )
                }
            }
        }
        // Fila inferior: 123 | ⌫
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KeyButton(
                label = "123",
                modifier = Modifier.weight(1.5f),
                labelSize = 13.sp,
                onClick = onToggleNumbers
            )
            BackspaceKey(modifier = Modifier.weight(1f), onClick = onBackspace)
        }
    }
}

@Composable
private fun KeyButton(
    label: String,
    modifier: Modifier = Modifier,
    labelSize: androidx.compose.ui.unit.TextUnit = 20.sp,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(CardBackground)
            .border(1.dp, CardBorder, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = labelSize,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun BackspaceKey(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(CardBackground)
            .border(1.dp, CardBorder, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Backspace,
            contentDescription = "Borrar",
            tint = GreyLavender,
            modifier = Modifier.size(22.dp)
        )
    }
}
