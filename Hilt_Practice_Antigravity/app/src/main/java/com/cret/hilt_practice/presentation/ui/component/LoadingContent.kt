package com.cret.hilt_practice.presentation.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cret.hilt_practice.R
import com.cret.hilt_practice.presentation.ui.theme.Hilt_PracticeTheme

@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    val loadingDescription = stringResource(R.string.content_desc_loading)
    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = loadingDescription },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp
        )
    }
}

// ──────────────── Previews ────────────────

@Preview(name = "LoadingContent – Light", showBackground = true)
@Composable
private fun LoadingContentPreview() {
    Hilt_PracticeTheme(dynamicColor = false) {
        LoadingContent()
    }
}

@Preview(
    name = "LoadingContent – Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun LoadingContentDarkPreview() {
    Hilt_PracticeTheme(darkTheme = true, dynamicColor = false) {
        LoadingContent()
    }
}
