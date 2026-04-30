package com.cret.hilt_practice.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.cret.hilt_practice.presentation.viewmodel.UserViewModel

@Composable
fun UserRoute(
    userId: String,
    viewModel: UserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.fetchUser(userId)
    }

    UserScreen(uiState = uiState)
}
