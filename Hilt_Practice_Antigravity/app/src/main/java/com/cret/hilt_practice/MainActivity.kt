package com.cret.hilt_practice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.cret.hilt_practice.presentation.ui.theme.Hilt_PracticeTheme
import com.cret.hilt_practice.presentation.ui.screen.UserScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hilt_PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UserScreen(
                        userId = "123",
                        // The modifier below isn't directly accepted by our UserScreen yet, 
                        // but normally we can wrap or modify. For simplicity we ignore innerPadding
                        // or we could add modifier to UserScreen.
                    )
                }
            }
        }
    }
}