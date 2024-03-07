package kanti.tododer.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.common.GroupUiState
import kanti.tododer.ui.common.MultiSelectionStyle
import kanti.tododer.ui.components.grouping.GroupHeader
import kanti.tododer.ui.components.menu.TodoGroupDropdownMenu
import kanti.tododer.ui.components.todo.TodoData

@Composable
fun TodoGroupPanel(
	modifier: Modifier = Modifier,
	selection: Boolean = false,
	isSingleGroup: Boolean = false,
	group: GroupUiState,
	allowGrouping: Boolean = true,
	selectionStyle: Set<MultiSelectionStyle> = setOf(MultiSelectionStyle.ColorFill),
	groupOnChangeExpand: (String?, Boolean) -> Unit = { _, _ -> },
	groupOnChangeSelect: (String?, Boolean) -> Unit = { _, _ -> },
	groupMenuOnChangeDone: (String?, Boolean) -> Unit = { _, _ -> },
	groupMenuOnRename: (String?) -> Unit = {},
	groupMenuOnUngroup: (String?) -> Unit = {},
	groupMenuOnSelect: (String?) -> Unit = {},
	groupMenuOnDeleteGroup: (String?) -> Unit = {},
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
				.fold(false) { acc, todoData -> acc or todoData }) {
			var expandMenu by rememberSaveable { mutableStateOf(false) }
			Row {
				Column {
					GroupHeader(
						title = group.name ?: stringResource(id = R.string.without_group),
						expanded = group.expand,
						onLongClick = { expandMenu = true },
						onChangeExpand = { expand ->
							groupOnChangeExpand(group.name, expand)
						}
					)
					TodoGroupDropdownMenu(
						expanded = expandMenu,
						todosDone = group.todos.fold(true) { acc, todoUiState ->
							acc and todoUiState.data.isDone
						},
						allowGrouping = allowGrouping,
						isGroup = group.name != null,
						onDismissRequest = { expandMenu = false },
						onChangeDone = { groupMenuOnChangeDone(group.name, it) },
						onRenameGroup = { groupMenuOnRename(group.name) },
						onUngroup = { groupMenuOnUngroup(group.name) },
						onSelect = { groupMenuOnSelect(group.name) },
						onDeleteGroup = { groupMenuOnDeleteGroup(group.name) }
					)
				}
				Box(
					modifier = Modifier.weight(1f),
					contentAlignment = Alignment.CenterEnd
				) {
					androidx.compose.animation.AnimatedVisibility(
						visible = selection,
						enter = fadeIn(),
						exit = fadeOut()
					) {
						Checkbox(
							modifier = Modifier.size(40.dp),
							checked = group.selected,
							onCheckedChange = { selected ->
								groupOnChangeSelect(group.name, selected)
							}
						)
					}
				}
			}
			Spacer(modifier = Modifier.height(height = 8.dp))
		}
		AnimatedVisibility(
			visible = group.expand || (isSingleGroup && group.name == null),
			enter = expandVertically() + fadeIn(),
			exit = shrinkVertically() + fadeOut(),
			label = group.name ?: "null"
		) {
			Column {
				for (todoUiState in group.todos) {
					SuperTodoCard(
						modifier = Modifier.padding(bottom = 8.dp),
						selectionStyle = selectionStyle,
						selection = selection,
						todoUiState = todoUiState,
						allowGrouping = allowGrouping,
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