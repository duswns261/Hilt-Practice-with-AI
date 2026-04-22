package com.cret.hilt_practice.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cret.hilt_practice.data.model.User
import com.cret.hilt_practice.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    fun fetchUser(userId: String) {
        _uiState.value = UserUiState.Loading
        viewModelScope.launch {
            try {
                val user = repository.getUser(userId)
                _uiState.value = if (user != null) {
                    UserUiState.Success(user)
                } else {
                    UserUiState.Error("사용자를 찾을 수 없습니다.")
                }
            } catch (e: Exception) {
                _uiState.value = UserUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }

    fun simulateLoading() {
        _uiState.value = UserUiState.Loading
    }

    fun simulateSuccess(userId: String) {
        _uiState.value = UserUiState.Success(User(id = userId, name = "Mock User"))
    }

    fun simulateError() {
        _uiState.value = UserUiState.Error("네트워크 오류가 발생했습니다.")
    }
}
