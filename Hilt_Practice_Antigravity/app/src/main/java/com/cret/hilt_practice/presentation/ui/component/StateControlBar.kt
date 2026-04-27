package com.cret.hilt_practice.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cret.hilt_practice.presentation.ui.theme.Hilt_PracticeTheme
import com.cret.hilt_practice.presentation.ui.screen.UserUiState

@Composable
fun StateControlBar(
    uiState: UserUiState,
    onLoadingClick: () -> Unit,
    onSuccessClick: () -> Unit,
    onErrorClick: () -> Unit
) {
    Surface(tonalElevation = 3.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            FilterChip(
                selected = uiState is UserUiState.Loading,
                onClick = onLoadingClick,
                label = { Text("로딩") }
            )
            FilterChip(
                selected = uiState is UserUiState.Success,
                onClick = onSuccessClick,
                label = { Text("성공") }
            )
            FilterChip(
                selected = uiState is UserUiState.Error,
                onClick = onErrorClick,
                label = { Text("오류") }
            )
        }
    }
}

// ──────────────── Previews ────────────────

@Preview(name = "StateControlBar – Loading", showBackground = true)
@Composable
private fun StateControlBarLoadingPreview() {
    Hilt_PracticeTheme(dynamicColor = false) {
        StateControlBar(
            uiState = UserUiState.Loading,
            onLoadingClick = {},
            onSuccessClick = {},
            onErrorClick = {}
        )
    }
}

@Preview(name = "StateControlBar – Success", showBackground = true)
@Composable
private fun StateControlBarSuccessPreview() {
    Hilt_PracticeTheme(dynamicColor = false) {
        StateControlBar(
            uiState = UserUiState.Success(com.cret.hilt_practice.data.model.User("user_123", "Mock User")),
            onLoadingClick = {},
            onSuccessClick = {},
            onErrorClick = {}
        )
    }
}
