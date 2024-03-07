package kanti.tododer.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kanti.tododer.feat.todo.R

@Composable
fun UngroupDialog(
	onDismissRequest: () -> Unit = {},
	onUngroup: () -> Unit = {}
) = AlertDialog(
	onDismissRequest = onDismissRequest,
	confirmButton = {
		TextButton(onClick = onUngroup) {
			Text(text = stringResource(id = R.string.ungroup))
		}
	},
	dismissButton = {
		TextButton(onClick = onDismissRequest) {
			Text(text = stringResource(id = R.string.cancel))
		}
	},
	title = {
		Text(text = stringResource(id = R.string.ungroup_group_ask))
	}
)

@Preview
@Composable
private fun PreviewUngroupDialog() {
	UngroupDialog()
}