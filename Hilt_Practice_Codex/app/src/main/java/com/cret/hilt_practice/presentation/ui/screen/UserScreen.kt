package com.cret.hilt_practice.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cret.hilt_practice.data.model.User
import com.cret.hilt_practice.presentation.ui.theme.Hilt_PracticeTheme
import com.cret.hilt_practice.presentation.viewmodel.UserViewModel
import com.cret.hilt_practice.presentation.viewmodel.UserViewModelFactory
import com.cret.hilt_practice.presentation.viewmodel.UserUiState

enum class UserScreenScenario(val label: String) {
    Loaded("Loaded"),
    Loading("Loading"),
    Error("Error")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    userId: String,
    viewModelFactory: UserViewModelFactory,
    viewModel: UserViewModel = viewModel(factory = viewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedScenario by rememberSaveable {
        mutableStateOf(UserScreenScenario.Loaded)
    }

    LaunchedEffect(userId) {
        viewModel.fetchUser(userId)
    }

    val displayUiState = when (selectedScenario) {
        UserScreenScenario.Loaded -> uiState
        UserScreenScenario.Loading -> UserUiState(isLoading = true)
        UserScreenScenario.Error -> UserUiState(
            errorMessage = "Unable to fetch profile for $userId"
        )
        else -> uiState
    }

    UserScreenContent(
        uiState = displayUiState,
        selectedScenario = selectedScenario,
        onScenarioSelected = { selectedScenario = it }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreenContent(
    uiState: UserUiState,
    selectedScenario: UserScreenScenario = UserScreenScenario.Loaded,
    onScenarioSelected: (UserScreenScenario) -> Unit = {},
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
            ScenarioSelector(
                selectedScenario = selectedScenario,
                onScenarioSelected = onScenarioSelected
            )

            UserOverviewCard(user = uiState.user)

            if (uiState.isLoading) {
                LoadingCard()
            }

            uiState.errorMessage?.let { message ->
                ErrorCard(message = message)
            }

            uiState.user?.let { user ->
                UserDetailsCard(user = user)
            } ?: run {
                if (!uiState.isLoading && uiState.errorMessage == null) {
                    EmptyStateCard()
                }
            }
        }
    }
}

@Composable
private fun ScenarioSelector(
    selectedScenario: UserScreenScenario,
    onScenarioSelected: (UserScreenScenario) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "State Playground",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Switch between the simple UI states while keeping the manual DI sample intact.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            UserScreenScenario.entries.forEach { scenario ->
                FilterChip(
                    selected = selectedScenario == scenario,
                    onClick = { onScenarioSelected(scenario) },
                    label = {
                        Text(text = scenario.label)
                    }
                )
            }
        }
    }
}

@Composable
private fun UserOverviewCard(user: User?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        ),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Profile Snapshot",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = user?.name ?: "Waiting for profile",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = user?.email ?: "User data will appear here",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                ProfileBadge(name = user?.name)
            }

            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OverviewMetric(
                        label = "Status",
                        value = if (user == null) "Pending" else "Active"
                    )
                    OverviewMetric(
                        label = "User ID",
                        value = user?.id ?: "Not loaded"
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileBadge(name: String?) {
    val initials = name
        ?.split(" ")
        ?.filter { it.isNotBlank() }
        ?.take(2)
        ?.joinToString("") { it.take(1).uppercase() }
        ?.ifBlank { "?" }
        ?: "?"

    Box(
        modifier = Modifier
            .size(68.dp)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.tertiary
                    )
                )
            ),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
    }
}

@Composable
private fun OverviewMetric(label: String, value: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.72f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun UserDetailsCard(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Contact Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            DetailRow(label = "Full name", value = user.name)
            DetailRow(label = "Email", value = user.email)
            DetailRow(label = "Member id", value = user.id)
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun LoadingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(42.dp),
                strokeWidth = 4.dp
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Loading user data",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Fetching the profile snapshot and contact details for this screen.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun EmptyStateCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "No profile selected",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Once a user is loaded, this area shows the key identity and contact information in a readable layout.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Could not load profile",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
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

@Preview(showBackground = true, name = "Error")
@Composable
private fun UserScreenErrorPreview() {
    Hilt_PracticeTheme(dynamicColor = false) {
        UserScreenContent(
            uiState = UserUiState(errorMessage = "Unable to fetch profile for preview-user")
        )
    }
}

@Preview(showBackground = true, name = "User Loaded Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun UserScreenContentDarkPreview() {
    Hilt_PracticeTheme(darkTheme = true, dynamicColor = false) {
        UserScreenContent(
            uiState = UserUiState(user = previewUser)
        )
    }
}
