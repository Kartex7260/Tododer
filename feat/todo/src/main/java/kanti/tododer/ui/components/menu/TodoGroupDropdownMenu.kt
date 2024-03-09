package kanti.tododer.ui.components.menu

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kanti.tododer.feat.todo.R

@Composable
fun TodoGroupDropdownMenu(
	expanded: Boolean,
	todosDone: Boolean = false,
	allowGrouping: Boolean = true,
	isGroup: Boolean = true,
	onDismissRequest: () -> Unit,
	onChangeDone: (Boolean) -> Unit = {},
	onRenameGroup: () -> Unit = {},
	onUngroup: () -> Unit = {},
	onSelect: () -> Unit = {},
	onDeleteGroup: () -> Unit = {}
) = DropdownMenu(expanded = expanded, onDismissRequest = onDismissRequest) {
	DropdownMenuItem(
		text = {
			Text(
				text = stringResource(
					id = if (todosDone) R.string.mark_as_not_completed
					else R.string.mark_as_completed
				)
			)
		},
		onClick = {
			onChangeDone(!todosDone)
			onDismissRequest()
		}
	)
	if (allowGrouping) {
		DropdownMenuItem(
			text = { Text(text = stringResource(id = R.string.rename)) },
			onClick = {
				onRenameGroup()
				onDismissRequest()
			}
		)
		if (isGroup) {
			DropdownMenuItem(
				text = { Text(text = stringResource(id = R.string.ungroup)) },
				onClick = {
					onUngroup()
					onDismissRequest()
				}
			)
		}
	}
	DropdownMenuItem(
		text = { Text(text = stringResource(id = R.string.select)) },
		onClick = {
			onSelect()
			onDismissRequest()
		}
	)
	if (allowGrouping) {
		DropdownMenuItem(
			text = { Text(text = stringResource(id = R.string.delete_group)) },
			onClick = {
				onDeleteGroup()
				onDismissRequest()
			}
		)
	}
}