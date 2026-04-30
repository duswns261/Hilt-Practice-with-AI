package com.cret.hilt_practice.presentation.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cret.hilt_practice.R
import com.cret.hilt_practice.presentation.ui.theme.Hilt_PracticeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    uiState: UserUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "User Profile",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            UserOverviewCard(user = uiState.user)

            if (uiState.isLoading) {
                LoadingCard()
            }

            uiState.errorMessageRes?.let { messageRes ->
                ErrorCard(message = stringResource(id = messageRes))
            }

            uiState.user?.let { user ->
                UserDetailsCard(user = user)
            } ?: run {
                if (!uiState.isLoading && uiState.errorMessageRes == null) {
                    EmptyStateCard()
                }
            }
        }
    }
}

private val screenPreviewUser = UserProfileUiModel(
    id = "preview-user",
    name = "Preview Student",
    email = "preview@example.com"
)

@Preview(showBackground = true, name = "User Loaded")
@Composable
private fun UserScreenLoadedPreview() {
    Hilt_PracticeTheme(dynamicColor = false) {
        UserScreen(
            uiState = UserUiState(user = screenPreviewUser)
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun UserScreenLoadingPreview() {
    Hilt_PracticeTheme(dynamicColor = false) {
        UserScreen(
            uiState = UserUiState(isLoading = true)
        )
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
private fun UserScreenErrorPreview() {
    Hilt_PracticeTheme(dynamicColor = false) {
        UserScreen(
            uiState = UserUiState(errorMessageRes = R.string.profile_error_user_not_found)
        )
    }
}

@Preview(
    showBackground = true,
    name = "User Loaded Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun UserScreenDarkPreview() {
    Hilt_PracticeTheme(darkTheme = true, dynamicColor = false) {
        UserScreen(
            uiState = UserUiState(user = screenPreviewUser)
        )
    }
}
