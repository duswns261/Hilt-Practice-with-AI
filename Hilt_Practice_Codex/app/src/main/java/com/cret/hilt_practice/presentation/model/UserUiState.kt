package com.cret.hilt_practice.presentation.model

import com.cret.hilt_practice.data.model.User

data class UserUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null
)
