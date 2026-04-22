package com.cret.hilt_practice.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cret.hilt_practice.data.model.User
import com.cret.hilt_practice.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UserUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null
)

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    fun fetchUser(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val userData = repository.getUser(userId)
                _uiState.value = UserUiState(
                    isLoading = false,
                    user = userData
                )
            } catch (exception: Exception) {
                _uiState.value = UserUiState(
                    isLoading = false,
                    errorMessage = exception.message ?: "Failed to load user"
                )
            }
        }
    }
}
