package com.cret.hilt_practice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.cret.hilt_practice.presentation.ui.screen.UserScreen
import com.cret.hilt_practice.presentation.ui.theme.Hilt_PracticeTheme
import com.cret.hilt_practice.presentation.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels {
        val repository = (application as HiltPracticeApplication).container.repository
        UserViewModel.factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hilt_PracticeTheme {
                UserScreen(
                    userId = "123",
                    viewModel = userViewModel
                )
            }
        }
    }
}
