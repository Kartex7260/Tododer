package kanti.tododer.ui.components.todo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TodoEditor(
	modifier: Modifier = Modifier,
	initialState: TodoData,
	strings: TodoEditorStrings = TodoEditorDefaults.strings(),
	onTitleChanged: (title: String) -> Unit = {},
	onRemarkChanged: (remark: String) -> Unit = {},
	onDoneChanged: (isDone: Boolean) -> Unit = {},
	preAction: @Composable RowScope.() -> Unit = {},
//	onArchive: () -> Unit = {},
	onDelete: () -> Unit = {}
) {
	Column(
		modifier = modifier
	) {
		var title by rememberSaveable(inputs = arrayOf(initialState)) {
			mutableStateOf(initialState.title)
		}
		OutlinedTextField(
			value = title,
			onValueChange = { newTitle ->
				title = newTitle
				onTitleChanged(newTitle)
			},
			modifier = Modifier
				.padding(
					start = 16.dp,
					top = 4.dp,
					end = 16.dp
				)
				.fillMaxWidth(),
			label = { Text(text = strings.title) }
		)

		var remark by rememberSaveable(inputs = arrayOf(initialState)) {
			mutableStateOf(initialState.remark)
		}
		OutlinedTextField(
			value = remark,
			onValueChange = { newRemark ->
				remark = newRemark
				onRemarkChanged(newRemark)
			},
			modifier = Modifier
				.padding(
					start = 16.dp,
					top = 4.dp,
					end = 16.dp
				)
				.fillMaxWidth(),
			label = { Text(text = strings.remark) }
		)

		TodoEditorControllers(
			modifier = Modifier
				.padding(
					top = 12.dp,
					start = 16.dp,
					end = 16.dp
				)
				.fillMaxWidth(),
			state = initialState,
			onDoneChanged = onDoneChanged,
			preAction = preAction,
//			onArchive = onArchive
			onDelete = onDelete
		)
	}
}

@Preview(showBackground = true)
@Composable
fun PreviewTodoEditor() {
	var title by remember {
		mutableStateOf("Test")
	}
	var remark by remember {
		mutableStateOf("")
	}
	var isDone by remember {
		mutableStateOf(false)
	}
	TodoEditor(
		initialState = TodoData(
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