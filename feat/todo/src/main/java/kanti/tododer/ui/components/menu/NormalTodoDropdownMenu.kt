package kanti.tododer.ui.components.menu

import android.annotation.SuppressLint
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kanti.tododer.feat.todo.R

@Composable
fun NormalTodoDropdownMenu(
	expanded: Boolean,
	onDismissRequest: () -> Unit = {},
	@SuppressLint("ModifierParameter")
	modifier: Modifier = Modifier,
	offset: DpOffset = DpOffset(0.dp, 0.dp),
	onRename: () -> Unit = {},
	onDelete: () -> Unit = {}
) {
	DropdownMenu(
		expanded = expanded,
		onDismissRequest = { onDismissRequest() },
		modifier = modifier,
		offset = offset
	) {
		DropdownMenuItem(
			text = { Text(text = stringResource(id = R.string.rename)) },
			onClick = {
				onRename()
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
private fun PreviewNormalMenuDropdownMenu() {
	NormalTodoDropdownMenu(expanded = true)
}