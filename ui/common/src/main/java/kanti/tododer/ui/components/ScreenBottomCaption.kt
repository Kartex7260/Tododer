package kanti.tododer.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ScreenBottomCaption(
    modifier: Modifier = Modifier,
    text: String
) = Text(
    modifier = modifier,
    style = MaterialTheme.typography.labelSmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .1f),
    textAlign = TextAlign.Center,
    text = text
)

@Preview
@Composable
private fun PreviewScreenBottomCaption() {
    ScreenBottomCaption(text = "Test text")
}