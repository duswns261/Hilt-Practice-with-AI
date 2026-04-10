package com.cret.hilt_practice.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cret.hilt_practice.data.model.User
import com.cret.hilt_practice.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    fun fetchUser(userId: String) {
        viewModelScope.launch {
            try {
                val userData = repository.getUser(userId)
                _user.value = userData
            } catch (e: Exception) {
                // Ignore error
            }
        }
    }
}