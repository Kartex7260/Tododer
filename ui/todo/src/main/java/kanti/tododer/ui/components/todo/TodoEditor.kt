package kanti.tododer.ui.components.todo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TodoEditor(
	modifier: Modifier = Modifier,
	state: TodoUiState,
	onTitleChanged: (title: String) -> Unit = {},
	onRemarkChanged: (remark: String) -> Unit = {},
	onDoneChanged: (isDone: Boolean) -> Unit = {},
//	onArchive: () -> Unit = {},
	onDelete: () -> Unit = {}
) {
	Column(
		modifier = modifier
	) {
		OutlinedTextField(
			value = state.title,
			onValueChange = onTitleChanged,
			modifier = Modifier
				.padding(
					start = 16.dp,
					top = 4.dp,
					end = 16.dp
				)
				.fillMaxWidth(),
			label = { Text(text = stringResource(id = R.string.title)) }
		)

		OutlinedTextField(
			value = state.remark,
			onValueChange = onRemarkChanged,
			modifier = Modifier
				.padding(
					start = 16.dp,
					top = 4.dp,
					end = 16.dp
				)
				.fillMaxWidth(),
			label = { Text(text = stringResource(id = R.string.remark)) }
		)

		TodoEditorControllers(
			modifier = Modifier
				.padding(
					top = 12.dp,
					start = 16.dp,
					end = 16.dp
				)
				.fillMaxWidth(),
			state = state,
			onDoneChanged = onDoneChanged,
//			onArchive = onArchive
			onDelete = onDelete
		)
	}
}

@Preview(showBackground = true)
@Composable
fun PreviewTodoEditor() {
	var title by remember {
		mutableStateOf("")
	}
	var remark by remember {
		mutableStateOf("")
	}
	var isDone by remember {
		mutableStateOf(false)
	}
	TodoEditor(
		state = TodoUiState(
			id = 1,
			title = title,
			remark = remark,
			isDone = isDone
		),
		onTitleChanged = { title = it },
		onRemarkChanged = { remark = it },
		onDoneChanged = { isDone = it }
	)
}