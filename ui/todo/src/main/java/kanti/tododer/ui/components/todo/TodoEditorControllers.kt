package kanti.tododer.ui.components.todo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TodoEditorControllers(
	modifier: Modifier = Modifier,
	state: TodoData,
	enabledDeleting: Boolean = true,
	onDoneChanged: (isDone: Boolean) -> Unit = {},
	preAction: @Composable RowScope.() -> Unit = {},
//	onArchive: () -> Unit = {},
	onDelete: () -> Unit = {}
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Checkbox(
			checked = state.isDone,
			onCheckedChange = onDoneChanged
		)
		Row {
			preAction()
//			IconButton(onClick = { onArchive() }) {
//				Icon(
//					painter = painterResource(id = R.drawable.outline_archive_24),
//					contentDescription = null
//				)
//			}
			IconButton(
				onClick = { onDelete() },
				enabled = enabledDeleting
			) {
				Icon(
					painter = painterResource(id = R.drawable.baseline_delete_forever_24),
					contentDescription = null
				)
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun PreviewTodoEditorControllers() {
	var isDone by remember {
		mutableStateOf(false)
	}
	TodoEditorControllers(
		modifier = Modifier
			.fillMaxWidth(),
		state = TodoData(
			id = 0,
			isDone = isDone
		),
		preAction = {
			IconButton(onClick = { }) {
				Icon(imageVector = Icons.Default.Clear, contentDescription = null)
			}
		},
		onDoneChanged = { isDone = it }
	)
}