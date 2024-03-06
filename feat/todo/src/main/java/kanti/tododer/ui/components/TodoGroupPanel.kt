package kanti.tododer.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.common.GroupUiState
import kanti.tododer.ui.common.MultiSelectionStyle
import kanti.tododer.ui.components.grouping.GroupHeader
import kanti.tododer.ui.components.todo.TodoData

@Composable
fun TodoGroupPanel(
	modifier: Modifier = Modifier,
	selection: Boolean = false,
	isSingleGroup: Boolean = false,
	group: GroupUiState,
	selectionStyle: Set<MultiSelectionStyle> = setOf(MultiSelectionStyle.ColorFill),
	itemOnLongClick: (TodoData) -> Unit = {},
	itemOnClick: (TodoData) -> Unit = {},
	itemOnDoneChange: (TodoData, Boolean) -> Unit = { _, _ -> },
	itemOnChangeSelect: (TodoData, Boolean) -> Unit = { _, _ -> },
	itemMenuOnAddToGroup: (TodoData) -> Unit = {},
	itemMenuOnRename: (TodoData) -> Unit = {},
	itemMenuOnDelete: (TodoData) -> Unit = {}
) {
	Column(
		modifier = modifier
	) {
		if ((!isSingleGroup || group.name != null) && group.todos.map { it.visible }
				.fold(true) { acc, todoData -> acc and todoData }) {
			GroupHeader(
				title = group.name ?: stringResource(id = R.string.ungroup),
				expanded = group.expand
			)
			Spacer(modifier = Modifier.height(height = 8.dp))
		}
		AnimatedVisibility(
			visible = group.expand || isSingleGroup,
			enter = expandVertically(),
			exit = shrinkVertically()
		) {
			Column {
				for (todoUiState in group.todos) {
					SuperTodoCard(
						modifier = Modifier.padding(bottom = 8.dp),
						selectionStyle = selectionStyle,
						selection = selection,
						todoUiState = todoUiState,
						onLongClick = itemOnLongClick,
						onClick = itemOnClick,
						onDoneChange = itemOnDoneChange,
						onChangeSelect = itemOnChangeSelect,
						menuOnAddToGroup = itemMenuOnAddToGroup,
						menuOnRename = itemMenuOnRename,
						menuOnDelete = itemMenuOnDelete
					)
				}
			}
		}
	}
}

@Preview
@Composable
private fun PreviewTodoGroupPanel() {
	TodoGroupPanel(group = GroupUiState())
}