package com.cret.hilt_practice.presentation.ui.screen

import com.cret.hilt_practice.data.model.User

sealed interface UserUiState {
    data object Loading : UserUiState
    data class Success(val user: User) : UserUiState
    data class Error(val message: String) : UserUiState
}
