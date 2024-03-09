package kanti.tododer.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kanti.tododer.feat.todo.R

@Composable
fun DeleteDialog(
    onCloseDialog: () -> Unit = {},
    icon: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
    delete: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = { onCloseDialog() },
        confirmButton = {
            TextButton(onClick = {
                delete()
                onCloseDialog()
            }) {
                Text(text = stringResource(id = R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = { onCloseDialog() }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        icon = icon,
        title = title,
        text = text
    )
}

@Preview
@Composable
private fun PreviewDeleteTodoDialog() {
    DeleteDialog(
        title = {
            Text(text = "Title")
        },
        text = {
            Text(text = "Test text")
        }
    )
}