package com.cret.hilt_practice.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cret.hilt_practice.presentation.viewmodel.UserViewModel
import com.cret.hilt_practice.presentation.viewmodel.UserViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    userId: String,
    viewModelFactory: UserViewModelFactory,
    viewModel: UserViewModel = viewModel(factory = viewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.fetchUser(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "User Profile") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (uiState.isLoading) {
                Text(text = "Loading user...")
            }

            uiState.user?.let { user ->
                Text(text = "id: ${user.id}")
                Text(text = "name: ${user.name}")
                Text(text = "email: ${user.email}")
            }
        }
    }
}
