package com.cret.hilt_practice.presentation.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cret.hilt_practice.presentation.ui.theme.Hilt_PracticeTheme

@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp
        )
    }
}

// ──────────────── Previews ────────────────

@Preview(name = "LoadingContent", showBackground = true)
@Composable
private fun LoadingContentPreview() {
    Hilt_PracticeTheme(dynamicColor = false) {
        LoadingContent()
    }
}
