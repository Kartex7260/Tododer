package kanti.tododer.ui.screen.plan_list

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kanti.tododer.feat.todo.R

@Composable
fun CreatePlanDialog(
	onCloseDialog: () -> Unit,
	create: (title: String) -> Unit
) {
	var titleState by remember {
		mutableStateOf("")
	}
	AlertDialog(
		onDismissRequest = { onCloseDialog() },
		confirmButton = {
			Button(
				onClick = {
					create(titleState)
					onCloseDialog()
				},
				enabled = titleState.isNotBlank()
			) {
				Text(text = stringResource(id = R.string.create))
			}
		},
		dismissButton = {
			Button(
				onClick = { onCloseDialog() }
			) {
				Text(text = stringResource(id = R.string.cancel))
			}
		},
		title = {
			Text(text = stringResource(id = R.string.create_new_plan))
		},
		text = {
			OutlinedTextField(
				value = titleState,
				onValueChange = { titleState = it },
				label = {
					Text(text = stringResource(id = R.string.plan_name))
				}
			)
		}
	)
}

@Preview
@Composable
private fun PreviewCreatePlanDialog() {
	CreatePlanDialog(onCloseDialog = {  }, create = {})
}