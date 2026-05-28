package com.cret.hilt_practice.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cret.hilt_practice.data.model.UserError
import com.cret.hilt_practice.domain.usecase.GetUserUseCase
import com.cret.hilt_practice.presentation.model.UserUiModel
import com.cret.hilt_practice.presentation.model.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    private var currentUserId: String? = null
    private var loadUserJob: Job? = null

    fun loadUserInformation(userId: String) {
        if (currentUserId == userId && _uiState.value is UserUiState.Success) {
            return
        }

        currentUserId = userId
        loadUser(userId)
    }

    private fun loadUser(userId: String) {
        loadUserJob?.cancel()

        loadUserJob = viewModelScope.launch {
            _uiState.value = UserUiState.Loading

            if (currentUserId != userId) return@launch

            _uiState.value = getUserUseCase(userId).fold(
                onSuccess = { user -> UserUiState.Success(user.toUiModel()) },
                onFailure = { throwable -> UserUiState.Error(UserError.from(throwable)) }
            )
        }
    }

    private fun com.cret.hilt_practice.data.model.User.toUiModel() =
        UserUiModel(id = id, displayName = name)
}
