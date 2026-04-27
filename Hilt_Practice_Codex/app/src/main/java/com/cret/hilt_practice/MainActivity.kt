package com.cret.hilt_practice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cret.hilt_practice.presentation.ui.screen.UserScreenRoute
import com.cret.hilt_practice.presentation.ui.theme.Hilt_PracticeTheme
import com.cret.hilt_practice.presentation.viewmodel.UserViewModelFactory

class MainActivity : ComponentActivity() {
    private val appContainer by lazy {
        (application as ManualDiApplication).appContainer
    }

    private val userViewModelFactory by lazy {
        UserViewModelFactory(repository = appContainer.userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Hilt_PracticeTheme {
                UserScreenRoute(
                    userId = "manual-di-user",
                    viewModelFactory = userViewModelFactory
                )
            }
        }
    }
}
