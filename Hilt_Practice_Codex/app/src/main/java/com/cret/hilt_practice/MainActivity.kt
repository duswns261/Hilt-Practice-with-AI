package com.cret.hilt_practice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cret.hilt_practice.di.AppContainer
import com.cret.hilt_practice.presentation.ui.screen.UserScreen
import com.cret.hilt_practice.presentation.ui.theme.Hilt_PracticeTheme
import com.cret.hilt_practice.presentation.viewmodel.UserViewModelFactory

class MainActivity : ComponentActivity() {
    private val appContainer = AppContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userViewModelFactory = UserViewModelFactory(
            repository = appContainer.userRepository
        )

        setContent {
            Hilt_PracticeTheme {
                UserScreen(
                    userId = "manual-di-user",
                    viewModelFactory = userViewModelFactory
                )
            }
        }
    }
}
