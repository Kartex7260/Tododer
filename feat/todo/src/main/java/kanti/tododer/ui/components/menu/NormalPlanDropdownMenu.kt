package kanti.tododer.ui.components.menu

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kanti.tododer.feat.todo.R

@Composable
fun NormalPlanDropdownMenu(
	expanded: Boolean = true,
	onDismissRequest: () -> Unit = {},
	onRename: () -> Unit = {},
	onSelect: () -> Unit = {},
	onDelete: () -> Unit = {}
) {
	DropdownMenu(expanded = expanded, onDismissRequest = onDismissRequest) {
		DropdownMenuItem(
			text = { Text(text = stringResource(id = R.string.rename)) },
			onClick = {
				onRename()
				onDismissRequest()
			}
		)
		DropdownMenuItem(
			text = { Text(text = stringResource(id = R.string.select)) },
			onClick = {
				onSelect()
				onDismissRequest()
			}
		)
		DropdownMenuItem(
			text = { Text(text = stringResource(id = R.string.delete)) },
			onClick = {
				onDelete()
				onDismissRequest()
			}
		)
	}
}

@Preview(showBackground = true)
@Composable
fun PreviewNormalPlanDropdownMenu() {
	NormalPlanDropdownMenu()
}