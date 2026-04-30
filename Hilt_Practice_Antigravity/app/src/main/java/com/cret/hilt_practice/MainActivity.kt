package com.cret.hilt_practice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cret.hilt_practice.presentation.ui.screen.UserRoute
import com.cret.hilt_practice.presentation.ui.theme.Hilt_PracticeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hilt_PracticeTheme {
                UserRoute(userId = DEMO_USER_ID)
            }
        }
    }

    companion object {
        private const val DEMO_USER_ID = "user_123"
    }
}
