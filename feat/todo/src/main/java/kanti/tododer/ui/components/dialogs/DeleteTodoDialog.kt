package kanti.tododer.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kanti.tododer.feat.todo.R

@Composable
fun DeleteTodoDialog(
	onCloseDialog: () -> Unit = {},
	todoTitle: String = "",
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
		text = {
			val firstFragment = stringResource(id = R.string.delete_ask_1)
			val secondFragment = stringResource(id = R.string.delete_ask_2_todo)
			Text(
				text = "$firstFragment \"$todoTitle\" $secondFragment?"
			)
		}
	)
}

@Preview
@Composable
private fun PreviewDeleteTodoDialog() {
	DeleteTodoDialog(
		todoTitle = "Test todo name"
	)
}