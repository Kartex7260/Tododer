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
	contentPadding: PaddingValues = PaddingValues(all = 16.dp),
	flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
	userScrollEnabled: Boolean = true,
	content: TodosUiState = TodosUiState(),
	onClick: (todo: TodoUiState) -> Unit,
	onDoneChanged: (isDone: Boolean, todo: TodoUiState) -> Unit,
	endButton: (@Composable (todo: TodoUiState) -> Unit)? = null
) = LazyColumn(
	modifier = modifier,
	state = state,
	contentPadding = contentPadding,
	flingBehavior = flingBehavior,
	userScrollEnabled = userScrollEnabled
) {
	items(
		items = content.todos,
		key = { it.id }
	) { uiState ->
		TodoCard(
			modifier = Modifier
				.padding(bottom = 16.dp),
			onClick = { onClick(uiState) },
			onDoneChange = { onDoneChanged(it, uiState) },
			todoUiState = uiState
		) {
			if (endButton != null) {
				endButton(uiState)
			}
		}
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
		content = TodosUiState(listOf(
			TodoUiState(0, "Hello", false),
			TodoUiState(1, "Ok", true),
			TodoUiState(2, "Foo", true)
		)),
		onClick = {},
		onDoneChanged = { _, _ ->  },
		endButton = {
			IconButton(onClick = {  }) {
				Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
			}
		}
	)
}