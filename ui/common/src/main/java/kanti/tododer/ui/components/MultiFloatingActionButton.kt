package kanti.tododer.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp

@Composable
fun MultiFloatingActionButton(
    altFab: Boolean = false,
    initExpandSmallFabState: Boolean = false,
    onClick: () -> Unit = {},
    smallFabContent: @Composable ColumnScope.(spacerBetweenSmallFab: @Composable () -> Unit) -> Unit,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(backgroundColor = containerColor),
    altFabContent: @Composable () -> Unit,
    fabContent: @Composable () -> Unit,
) {
    val spacerBetweenSmallFab = @Composable {
        Spacer(modifier = Modifier.height(height = 8.dp))
    }
    var expandSmallFab by rememberSaveable { mutableStateOf(initExpandSmallFabState) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = expandSmallFab && altFab,
            enter = expandVertically(
                expandFrom = Alignment.Top
            ),
            exit = shrinkVertically(
                shrinkTowards = Alignment.Top
            )
        ) {
            Column {
                smallFabContent(spacerBetweenSmallFab)
                Spacer(modifier = Modifier.height(height = 20.dp))
            }
        }
        FloatingActionButton(
            onClick = {
                if (altFab) {
                    expandSmallFab = !expandSmallFab
                } else onClick()
            },
            containerColor = containerColor,
            contentColor = contentColor,
            content = if (altFab) altFabContent else fabContent
        )
    }
}

@Preview
@Composable
private fun PreviewMultiFloatingActionButton(
    @PreviewParameter(FabType::class) fabType: Boolean
) {
    MultiFloatingActionButton(
        altFab = fabType,
        initExpandSmallFabState = true,
        smallFabContent = { spacerBetweenSmallFab ->
            SmallFloatingActionButton(onClick = { }) {
                Icon(imageVector = Icons.Default.Create, contentDescription = null)
            }
            spacerBetweenSmallFab()
            SmallFloatingActionButton(onClick = { }) {
                Icon(imageVector = Icons.Default.Create, contentDescription = null)
            }
        },
        altFabContent = {
            Icon(imageVector = Icons.Default.List, contentDescription = null)
        }
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }
}

private class FabType : CollectionPreviewParameterProvider<Boolean>(listOf(true, false))