package com.example.mendacium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mendacium.ui.navigation.AppNavigation
import com.example.mendacium.ui.theme.MendaciumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MendaciumTheme {
                AppNavigation()
            }
        }
    }
}
