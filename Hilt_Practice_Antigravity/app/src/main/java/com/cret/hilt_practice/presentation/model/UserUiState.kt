package com.cret.hilt_practice.presentation.model

import com.cret.hilt_practice.data.model.UserError

sealed interface UserUiState {
    data object Loading : UserUiState
    data class Success(val user: UserUiModel) : UserUiState
    data class Error(val error: UserError) : UserUiState
}
