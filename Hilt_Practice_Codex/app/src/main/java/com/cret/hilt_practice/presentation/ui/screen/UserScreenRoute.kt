package com.cret.hilt_practice.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cret.hilt_practice.BuildConfig
import com.cret.hilt_practice.R
import com.cret.hilt_practice.presentation.viewmodel.UserViewModel

@Composable
fun UserScreenRoute(
    userId: String?,
    viewModel: UserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedDebugState by rememberSaveable {
        mutableStateOf(UserScreenDebugState.Loaded)
    }

    LaunchedEffect(userId) {
        if (userId.isNullOrBlank()) {
            viewModel.showMissingUserIdError()
        } else {
            viewModel.fetchUser(userId)
        }
    }

    val displayUiState = if (BuildConfig.DEBUG) {
        when (selectedDebugState) {
            UserScreenDebugState.Loaded -> uiState
            UserScreenDebugState.Loading -> UserUiState(isLoading = true)
            UserScreenDebugState.Error -> UserUiState(
                errorMessageRes = R.string.profile_error_user_not_found
            )
        }
    } else {
        uiState
    }

    UserScreen(
        uiState = displayUiState,
        debugControls = if (BuildConfig.DEBUG) {
            {
                DebugStateControls(
                    selectedState = selectedDebugState,
                    onStateSelected = { selectedDebugState = it }
                )
            }
        } else {
            null
        }
    )
}
