package com.example.mendacium.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendacium.ui.theme.CardBackground
import com.example.mendacium.ui.theme.CardBorder
import com.example.mendacium.ui.theme.DarkBackground
import com.example.mendacium.ui.theme.GreyLavender
import com.example.mendacium.ui.theme.PurpleAccent

@Composable
fun PlayerCountSelector(
    value: Int,
    minValue: Int,
    maxValue: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    val progress = (value - minValue).toFloat() / (maxValue - minValue).toFloat()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardBackground)
            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(DarkBackground)
                    .border(1.dp, CardBorder, RoundedCornerShape(8.dp))
                    .clickable(onClick = onDecrease),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Remove,
                    contentDescription = "Decrementar",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = value.toString(),
                color = Color.White,
                fontSize = 52.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PurpleAccent)
                    .clickable(onClick = onIncrease),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Incrementar",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(CardBorder)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(2.dp))
                    .background(PurpleAccent)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("MÍN $minValue", color = GreyLavender, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text("MÁX $maxValue", color = GreyLavender, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RoleListCard(
    name: String,
    roleColor: Color,
    icon: ImageVector,
    value: Int,
    enabled: Boolean = true,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardBackground)
            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(roleColor.copy(alpha = 0.15f))
                .border(1.dp, roleColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = roleColor,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = name,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onDecrease,
                modifier = Modifier.size(32.dp),
                enabled = enabled
            ) {
                Icon(
                    Icons.Default.Remove,
                    contentDescription = "Decrementar",
                    tint = if (enabled) GreyLavender else GreyLavender.copy(alpha = 0.3f),
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = value.toString(),
                color = if (value > 0) roleColor else GreyLavender,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(28.dp),
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = onIncrease,
                modifier = Modifier.size(32.dp),
                enabled = enabled
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Incrementar",
                    tint = if (enabled) GreyLavender else GreyLavender.copy(alpha = 0.3f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun NumericSelector(
    value: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    textColor: Color = Color.White
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onDecrease,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                Icons.Default.Remove,
                contentDescription = "Decrementar",
                tint = Color.Gray
            )
        }
        Text(
            text = value.toString(),
            color = textColor,
            fontSize = 24.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        IconButton(
            onClick = onIncrease,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Incrementar",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun RoleConfigCard(
    title: String,
    titleColor: Color,
    icon: ImageVector,
    value: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardBackground)
            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = titleColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            title,
            color = titleColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        NumericSelector(
            value = value,
            onDecrease = onDecrease,
            onIncrease = onIncrease
        )
    }
}