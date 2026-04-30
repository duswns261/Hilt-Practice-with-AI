package com.cret.hilt_practice.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cret.hilt_practice.R
import com.cret.hilt_practice.domain.error.UserNotFoundException
import com.cret.hilt_practice.domain.model.UserProfile
import com.cret.hilt_practice.domain.usecase.GetUserProfileUseCase
import com.cret.hilt_practice.presentation.ui.screen.UserProfileUiModel
import com.cret.hilt_practice.presentation.ui.screen.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    fun fetchUser(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessageRes = null
            )

            try {
                val userData = getUserProfileUseCase(userId)
                _uiState.value = UserUiState(
                    isLoading = false,
                    user = userData.toUiModel()
                )
            } catch (exception: UserNotFoundException) {
                _uiState.value = UserUiState(
                    isLoading = false,
                    errorMessageRes = R.string.profile_error_user_not_found
                )
            }
        }
    }

    fun showMissingUserIdError() {
        _uiState.value = UserUiState(
            isLoading = false,
            errorMessageRes = R.string.profile_error_missing_user_id
        )
    }

    private fun UserProfile.toUiModel(): UserProfileUiModel {
        return UserProfileUiModel(
            id = id,
            name = name,
            email = email
        )
    }
}
