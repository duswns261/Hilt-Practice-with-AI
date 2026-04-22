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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cret.hilt_practice.data.model.User
import com.cret.hilt_practice.presentation.ui.theme.Hilt_PracticeTheme
import com.cret.hilt_practice.presentation.viewmodel.UserViewModel
import com.cret.hilt_practice.presentation.viewmodel.UserViewModelFactory
import com.cret.hilt_practice.presentation.viewmodel.UserUiState

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

    UserScreenContent(uiState = uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreenContent(
    uiState: UserUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
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

private val previewUser = User(
    id = "preview-user",
    name = "Preview Student",
    email = "preview@example.com"
)

@Preview(showBackground = true, name = "User Loaded")
@Composable
private fun UserScreenContentPreview() {
    Hilt_PracticeTheme(dynamicColor = false) {
        UserScreenContent(
            uiState = UserUiState(user = previewUser)
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun UserScreenLoadingPreview() {
    Hilt_PracticeTheme(dynamicColor = false) {
        UserScreenContent(
            uiState = UserUiState(isLoading = true)
        )
    }
}
