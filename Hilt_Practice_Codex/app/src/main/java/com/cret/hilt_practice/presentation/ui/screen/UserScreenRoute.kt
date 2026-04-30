package com.cret.hilt_practice.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cret.hilt_practice.presentation.viewmodel.UserViewModel

@Composable
fun UserScreenRoute(
    userId: String?,
    viewModel: UserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        if (userId.isNullOrBlank()) {
            viewModel.showMissingUserIdError()
        } else {
            viewModel.fetchUser(userId)
        }
    }

    UserScreen(
        uiState = uiState
    )
}
