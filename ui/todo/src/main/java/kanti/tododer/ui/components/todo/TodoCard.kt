package kanti.tododer.ui.components.todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.unit.dp

@Composable
fun TodoCard(
	modifier: Modifier = Modifier,
	todoUiState: TodoUiState,
	onClick: () -> Unit,
	onDoneChange: (Boolean) -> Unit,
	endButton: (@Composable () -> Unit)? = null
) = OutlinedCard(
	modifier = modifier
		.clickable(onClick = onClick)
) {
	Column(
		modifier = Modifier
			.padding(
				start = 16.dp,
				end = 4.dp,
				top = 12.dp,
				bottom = 12.dp
			)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
		) {
			Checkbox(
				checked = todoUiState.isDone,
				onCheckedChange = onDoneChange
			)

			Spacer(
				modifier = Modifier.width(width = 16.dp)
			)

			Text(
				modifier = Modifier.weight(1f),
				text = todoUiState.title,
				style = MaterialTheme.typography.titleLarge,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)

			Spacer(
				modifier = Modifier.width(width = 16.dp)
			)

			if (endButton != null) {
				endButton()
			}
		}
	}
}

@Preview(
	showBackground = true
)
@Composable
fun PreviewTodoCard() {
	var todoUiState by remember {
		mutableStateOf(TodoUiState(0, "State test", false))
	}
	TodoCard(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp),
		onClick = {},
		todoUiState = todoUiState,
		onDoneChange = { isDone ->
			todoUiState = todoUiState.copy(isDone = isDone)
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