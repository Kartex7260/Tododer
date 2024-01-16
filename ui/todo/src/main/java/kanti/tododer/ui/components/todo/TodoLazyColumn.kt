package kanti.tododer.ui.components.todo

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TodoLazyColumn(
	modifier: Modifier = Modifier,
	state: LazyListState = rememberLazyListState(),
	contentPadding: PaddingValues = PaddingValues(
		top = 12.dp,
		bottom = 12.dp,
		start = 16.dp,
		end = 16.dp
	),
	flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
	userScrollEnabled: Boolean = true,
	preContent: @Composable () -> Unit = {},
	content: TodosData = TodosData(),
	postContent: @Composable () -> Unit = {},
	onClick: (todo: TodoData) -> Unit,
	onDoneChanged: (isDone: Boolean, todo: TodoData) -> Unit,
	actions: @Composable (todo: TodoData) -> Unit = {}
) = LazyColumn(
	modifier = modifier,
	state = state,
	contentPadding = contentPadding,
	flingBehavior = flingBehavior,
	userScrollEnabled = userScrollEnabled
) {
	item {
		preContent()
	}
	items(
		items = content.todos,
		key = { it.id }
	) { uiState ->
		TodoCard(
			modifier = Modifier
				.padding(bottom = 8.dp),
			onClick = { onClick(uiState) },
			onDoneChange = { onDoneChanged(it, uiState) },
			todoData = uiState
		) { actions(uiState) }
	}
	item {
		postContent()
	}
}

@Preview(
	showBackground = true
)
@Composable
private fun PreviewTodoLazyColumn() {
	TodoLazyColumn(
		modifier = Modifier
			.fillMaxSize(),
		content = TodosData(listOf(
			TodoData(0, "Hello", "", false),
			TodoData(1, "Ok", "", true),
			TodoData(2, "Foo", "", true)
		)),
		onClick = {},
		onDoneChanged = { _, _ ->  },
		actions = {
			IconButton(onClick = {  }) {
				Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
			}
		}
	)
}