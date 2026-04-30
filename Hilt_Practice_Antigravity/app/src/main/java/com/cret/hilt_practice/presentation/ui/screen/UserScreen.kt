package com.cret.hilt_practice.presentation.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.cret.hilt_practice.R
import com.cret.hilt_practice.data.model.UserError
import com.cret.hilt_practice.presentation.model.UserUiModel
import com.cret.hilt_practice.presentation.model.UserUiState
import com.cret.hilt_practice.presentation.ui.component.ErrorContent
import com.cret.hilt_practice.presentation.ui.component.LoadingContent
import com.cret.hilt_practice.presentation.ui.component.UserProfile
import com.cret.hilt_practice.presentation.ui.theme.Hilt_PracticeTheme

@Composable
fun UserScreen(uiState: UserUiState) {
    UserScreenContent(uiState = uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserScreenContent(
    uiState: UserUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "프로필",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is UserUiState.Loading -> LoadingContent(
                modifier = Modifier.padding(paddingValues)
            )
            is UserUiState.Success -> UserProfile(
                user = uiState.user,
                modifier = Modifier.padding(paddingValues)
            )
            is UserUiState.Error -> ErrorContent(
                message = stringResource(uiState.error.toStringRes()),
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

private fun UserError.toStringRes(): Int = when (this) {
    UserError.NotFound -> R.string.error_user_not_found
    UserError.Network -> R.string.error_network
    UserError.Unknown -> R.string.error_unknown
}

// ──────────────── Previews ────────────────

private val previewUser = UserUiModel(id = "user_123", displayName = "Mock User")

@Preview(name = "UserScreen – Loading", showBackground = true)
@Composable
private fun PreviewLoading() {
    Hilt_PracticeTheme(dynamicColor = false) {
        UserScreen(uiState = UserUiState.Loading)
    }
}

@Preview(name = "UserScreen – Success · Light", showBackground = true)
@Composable
private fun PreviewSuccess() {
    Hilt_PracticeTheme(dynamicColor = false) {
        UserScreen(uiState = UserUiState.Success(previewUser))
    }
}

@Preview(
    name = "UserScreen – Success · Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewSuccessDark() {
    Hilt_PracticeTheme(darkTheme = true, dynamicColor = false) {
        UserScreen(uiState = UserUiState.Success(previewUser))
    }
}

@Preview(name = "UserScreen – Error", showBackground = true)
@Composable
private fun PreviewError() {
    Hilt_PracticeTheme(dynamicColor = false) {
        UserScreen(uiState = UserUiState.Error(UserError.Network))
    }
}
