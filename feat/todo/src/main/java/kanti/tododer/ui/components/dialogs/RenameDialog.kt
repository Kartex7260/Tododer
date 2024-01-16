package kanti.tododer.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kanti.tododer.feat.todo.R

@Composable
fun RenameDialog(
	onCloseDialog: () -> Unit = {},
	title: (@Composable () -> Unit)? = null,
	label: (@Composable () -> Unit)? = null,
	name: String,
	onRename: (newName: String) -> Unit = {}
) {
	var newName by rememberSaveable(inputs = arrayOf(name)) {
		mutableStateOf(name)
	}
	AlertDialog(
		onDismissRequest = { onCloseDialog() },
		confirmButton = {
			TextButton(
				onClick = {
					onRename(newName)
					onCloseDialog()
				}
			) {
				Text(text = stringResource(id = R.string.rename))
			}
		},
		dismissButton = {
			TextButton(onClick = { onCloseDialog() }) {
				Text(text = stringResource(id = R.string.cancel))
			}
		},
		title = title,
		text = {
			OutlinedTextField(
				value = newName,
				onValueChange = { newName = it },
				label = label
			)
		}
	)
}

@Preview
@Composable
private fun PreviewRenameDialog() {
	var name by remember {
		mutableStateOf("Test")
	}
	RenameDialog(
		name = name,
		title = {
			Text(text = "Test title")
		},
		label = { Text(text = "Test label") },
		onRename = { newName ->
			name = newName
		}
	)
}