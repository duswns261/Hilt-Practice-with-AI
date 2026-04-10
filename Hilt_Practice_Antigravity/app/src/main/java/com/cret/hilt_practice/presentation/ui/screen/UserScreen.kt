package com.cret.hilt_practice.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cret.hilt_practice.data.model.User
import com.cret.hilt_practice.data.repository.UserRepository
import com.cret.hilt_practice.presentation.viewmodel.UserViewModel
import com.cret.hilt_practice.presentation.viewmodel.UserViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    userId: String,
    viewModel: UserViewModel = viewModel(factory = UserViewModelFactory(UserRepository()))
) {
    val user = viewModel.user.value

    LaunchedEffect(userId) {
        viewModel.fetchUser(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "User Profile") })
        }
    ) { paddingValues ->
        if (user != null) {
            UserProfile(user = user, modifier = Modifier.padding(paddingValues))
        } else {
            Text(text = "Loading...", modifier = Modifier.padding(paddingValues))
        }
    }
}

@Composable
fun UserProfile(user: User, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "User ID: ${user.id}")
        Text(text = "Name: ${user.name}")
    }
}