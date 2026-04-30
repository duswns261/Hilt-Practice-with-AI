package com.cret.hilt_practice.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cret.hilt_practice.data.model.UserError
import com.cret.hilt_practice.domain.usecase.GetUserUseCase
import com.cret.hilt_practice.presentation.model.UserUiModel
import com.cret.hilt_practice.presentation.model.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun fetchUser(userId: String) {
        _uiState.value = UserUiState.Loading
        viewModelScope.launch {
            _uiState.value = getUserUseCase(userId).fold(
                onSuccess = { user -> UserUiState.Success(user.toUiModel()) },
                onFailure = { throwable -> UserUiState.Error(UserError.from(throwable)) }
            )
        }
    }

    private fun com.cret.hilt_practice.data.model.User.toUiModel() =
        UserUiModel(id = id, displayName = name)
}
