package kanti.tododer.ui.components.selection

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SelectionBox(
    modifier: Modifier = Modifier,
    selection: Boolean = false,
    selected: Boolean = false,
    onChangeSelected: (Boolean) -> Unit = {},
    content: @Composable () -> Unit
) = Box(
    modifier = modifier
) {
    var checkBoxWidth by remember { mutableStateOf(0.dp) }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.CenterEnd
    ) {
        val density = LocalDensity.current
        Checkbox(
            modifier = Modifier
                .onGloballyPositioned {
                    checkBoxWidth = with(density) { it.size.width.toDp() }
                }
                .padding(
                    start = 4.dp
                ),
            checked = selected,
            onCheckedChange = onChangeSelected
        )
    }

    val contentPadding by animateDpAsState(
        targetValue = if (selection) checkBoxWidth else 0.dp,
        label = "contentPadding"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = contentPadding)
    ) {
        content()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun PreviewSelectionBox() {
    var selection by remember { mutableStateOf(false) }
    SelectionBox(
        modifier = Modifier.size(200.dp),
        selection = selection
    ) {
        Box(
            modifier = Modifier
                .background(Color.Green)
                .size(200.dp)
                .combinedClickable(
                    onClick = {
                        selection = false
                    },
                    onLongClick = {
                        selection = true
                    }
                )
        ) {
        }
    }
}