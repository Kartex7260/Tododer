package kanti.tododer.ui.components.dialogs

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import kanti.tododer.feat.todo.R

@Composable
fun CreateDialog(
	onCloseDialog: () -> Unit = {},
	title: (@Composable () -> Unit)? = null,
	textFieldLabel: (@Composable () -> Unit)? = null,
	add: ((title: String) -> Unit)? = null,
	create: (title: String) -> Unit = {}
) {
	var titleState by remember {
		mutableStateOf("")
	}
	val onAdd = {
		add?.invoke(titleState)
		titleState = ""
	}
	val onCreate = {
		create(titleState)
		onCloseDialog()
	}
	AlertDialog(
		onDismissRequest = { onCloseDialog() },
		confirmButton = {
			if (add != null) {
				TextButton(
					onClick = onAdd,
					enabled = titleState.isNotBlank()
				) {
					Text(text = stringResource(id = R.string.add))
				}
			}

			TextButton(
				onClick = onCreate,
				enabled = titleState.isNotBlank()
			) {
				Text(text = stringResource(id = R.string.create))
			}
		},
		dismissButton = {
			TextButton(
				onClick = { onCloseDialog() }
			) {
				Text(text = stringResource(id = R.string.cancel))
			}
		},
		title = title,
		text = {
			OutlinedTextField(
				value = titleState,
				onValueChange = { titleState = it },
				label = textFieldLabel,
				singleLine = true,
				keyboardOptions = KeyboardOptions(
					capitalization = KeyboardCapitalization.Sentences,
					imeAction = ImeAction.Next
				),
				keyboardActions = KeyboardActions(
					onNext = {
						if (add == null) {
							onCreate()
						} else {
							onAdd()
						}
					}
				)
			)
		}
	)
}

@Preview
@Composable
private fun PreviewCreateDialog() {
	CreateDialog(
		title = {
			Text(text = "Title")
		},
		textFieldLabel = {
			Text(text = "Label")
		}
	)
}