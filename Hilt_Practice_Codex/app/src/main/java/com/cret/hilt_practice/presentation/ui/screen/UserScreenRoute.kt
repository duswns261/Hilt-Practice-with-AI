package com.cret.hilt_practice.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cret.hilt_practice.presentation.viewmodel.UserViewModel
import com.cret.hilt_practice.presentation.viewmodel.UserViewModelFactory
import com.cret.hilt_practice.presentation.viewmodel.UserUiState

@Composable
fun UserScreenRoute(
    userId: String,
    viewModelFactory: UserViewModelFactory,
    viewModel: UserViewModel = viewModel(factory = viewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedScenario by rememberSaveable {
        mutableStateOf(UserScreenScenario.Loaded)
    }

    LaunchedEffect(userId) {
        viewModel.fetchUser(userId)
    }

    val displayUiState = when (selectedScenario) {
        UserScreenScenario.Loaded -> uiState
        UserScreenScenario.Loading -> UserUiState(isLoading = true)
        UserScreenScenario.Error -> UserUiState(
            errorMessage = "Unable to fetch profile for $userId"
        )
    }

    UserScreen(
        uiState = displayUiState,
        selectedScenario = selectedScenario,
        onScenarioSelected = { selectedScenario = it }
    )
}
