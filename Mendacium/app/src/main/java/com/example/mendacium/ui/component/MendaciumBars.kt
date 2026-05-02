package com.example.mendacium.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.mendacium.ui.theme.DarkBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarSimple () {
    TopAppBar(
        title = { Text("Mendacium", color = Color.White) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
    )
}

@Composable
fun NavigationBarSimple() {
    NavigationBar(containerColor = DarkBackground) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Home, contentDescription = "Juego") },
            label = { Text("JUEGO") }
        )
    }
}