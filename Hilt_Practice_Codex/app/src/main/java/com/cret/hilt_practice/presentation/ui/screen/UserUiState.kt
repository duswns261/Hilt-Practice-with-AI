package com.cret.hilt_practice.presentation.ui.screen

import androidx.annotation.StringRes

data class UserUiState(
    val isLoading: Boolean = false,
    val user: UserProfileUiModel? = null,
    @StringRes val errorMessageRes: Int? = null
)
