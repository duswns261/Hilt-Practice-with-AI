package com.cret.hilt_practice.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cret.hilt_practice.data.model.UserError
import com.cret.hilt_practice.presentation.model.UserUiModel
import com.cret.hilt_practice.presentation.model.UserUiState
import com.cret.hilt_practice.presentation.viewmodel.UserViewModel

@Composable
fun DebugUserRoute(
    userId: String,
    viewModel: UserViewModel = hiltViewModel()
) {
    val actualState by viewModel.uiState.collectAsState()
    var debugOverride by remember { mutableStateOf<UserUiState?>(null) }
    val displayState = debugOverride ?: actualState

    LaunchedEffect(userId) {
        viewModel.fetchUser(userId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        UserScreen(uiState = displayState)
        DebugStateBar(
            onStateSelected = { debugOverride = it },
            onReset = { debugOverride = null },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
        )
    }
}

private val debugStates: List<Pair<String, UserUiState?>> = listOf(
    "실제" to null,
    "로딩" to UserUiState.Loading,
    "성공" to UserUiState.Success(UserUiModel(id = "debug_01", displayName = "Debug User")),
    "오류: 네트워크" to UserUiState.Error(UserError.Network),
    "오류: 없음" to UserUiState.Error(UserError.NotFound),
    "오류: 알 수 없음" to UserUiState.Error(UserError.Unknown),
)

@Composable
private fun DebugStateBar(
    onStateSelected: (UserUiState) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Text(
                text = "DEBUG",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(debugStates) { (label, state) ->
                    FilterChip(
                        selected = false,
                        onClick = { if (state == null) onReset() else onStateSelected(state) },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }
        }
    }
}
