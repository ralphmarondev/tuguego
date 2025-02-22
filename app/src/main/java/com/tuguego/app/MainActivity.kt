package com.tuguego.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tuguego.app.features.home.presentation.map.MapScreen
import com.tuguego.app.ui.theme.TugueGoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TugueGoTheme {
                MapScreen()
            }
        }
    }
}