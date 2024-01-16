package kanti.tododer.ui.components.todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp

@Composable
fun TodoCard(
	modifier: Modifier = Modifier,
	todoData: TodoData,
	onClick: () -> Unit = {},
	onDoneChange: (Boolean) -> Unit = {},
	action: @Composable () -> Unit = {}
) = Card(
	modifier = modifier
		.clickable(onClick = onClick)
		.height(56.dp)
) {
	Column(
		modifier = Modifier
			.padding(
				start = 8.dp,
				end = 4.dp,
				top = 8.dp,
				bottom = 8.dp
			)
			.fillMaxSize(),
		verticalArrangement = Arrangement.Center
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
		) {
			Checkbox(
				checked = todoData.isDone,
				onCheckedChange = onDoneChange
			)

			Spacer(
				modifier = Modifier.width(width = 4.dp)
			)

			Text(
				modifier = Modifier.weight(1f),
				text = todoData.title,
				style = MaterialTheme.typography.titleMedium,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)

			Spacer(
				modifier = Modifier.width(width = 8.dp)
			)

			Box {
				action()
			}
		}
	}
}

@Preview(
	showBackground = true
)
@Composable
fun PreviewTodoCard(
	@PreviewParameter(PreviewTodoCardParameterProvider::class) todo: TodoData
) {
	var todoData by remember {
		mutableStateOf(todo)
	}
	TodoCard(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp),
		onClick = {},
		todoData = todoData,
		onDoneChange = { isDone ->
			todoData = todoData.copy(isDone = isDone)
		}
	) {
		IconButton(onClick = {  }) {
			Icon(
				imageVector = Icons.Default.MoreVert,
				contentDescription = null
			)
		}
	}
}

@Preview(
	showBackground = true
)
@Composable
fun PreviewTodoCardWitoutActions(
	@PreviewParameter(PreviewTodoCardParameterProvider::class) todo: TodoData
) {
	var todoData by remember {
		mutableStateOf(todo)
	}
	TodoCard(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp),
		onClick = {},
		todoData = todoData,
		onDoneChange = { isDone ->
			todoData = todoData.copy(isDone = isDone)
		}
	)
}

private class PreviewTodoCardParameterProvider : PreviewParameterProvider<TodoData> {

	override val values: Sequence<TodoData> = sequenceOf(
		TodoData(
			id = 1,
			title = "State test"
		),
		TodoData(
			id = 2,
			title = "Very large title, its over long title, very long"
		)
	)
}