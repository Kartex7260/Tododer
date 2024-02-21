package kanti.tododer.ui.components.dialogs

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import kanti.tododer.feat.todo.R

@Composable
fun RenameDialog(
	onCloseDialog: () -> Unit = {},
	title: (@Composable () -> Unit)? = null,
	label: (@Composable () -> Unit)? = null,
	name: String,
	allowEmptyName: Boolean = false,
	onRename: (newName: String) -> Unit = {}
) {
	var newName by rememberSaveable(inputs = arrayOf(name)) {
		mutableStateOf(name)
	}
	fun hasRename() = newName.isNotBlank() || allowEmptyName
	val rename = {
		onRename(newName)
		onCloseDialog()
	}
	AlertDialog(
		onDismissRequest = { onCloseDialog() },
		confirmButton = {
			TextButton(
				onClick = rename,
				enabled = hasRename()
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
				label = label,
				trailingIcon = if (newName.isNotEmpty()) {
					{
						IconButton(onClick = { newName = "" }) {
							Icon(
								imageVector = Icons.Default.Clear,
								contentDescription = null
							)
						}
					}
				} else null,
				keyboardOptions = KeyboardOptions(
					capitalization = KeyboardCapitalization.Sentences,
					imeAction = ImeAction.Next
				),
				keyboardActions = KeyboardActions(
					onNext = {
						if (hasRename())
							rename()
					}
				)
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