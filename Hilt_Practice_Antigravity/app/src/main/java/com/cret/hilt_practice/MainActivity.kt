package com.cret.hilt_practice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cret.hilt_practice.data.repository.UserRepositoryImpl
import com.cret.hilt_practice.presentation.ui.screen.UserScreen
import com.cret.hilt_practice.presentation.ui.theme.Hilt_PracticeTheme
import com.cret.hilt_practice.presentation.viewmodel.UserViewModelFactory

class MainActivity : ComponentActivity() {

    // 의존성 그래프를 Activity 레벨에서 조립
    private val repository = UserRepositoryImpl()
    private val viewModelFactory = UserViewModelFactory(repository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hilt_PracticeTheme {
                UserScreen(
                    userId = "123",
                    viewModelFactory = viewModelFactory
                )
            }
        }
    }
}
