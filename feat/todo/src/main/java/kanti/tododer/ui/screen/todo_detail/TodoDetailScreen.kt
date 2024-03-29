package kanti.tododer.ui.screen.todo_detail

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.UiConst
import kanti.tododer.ui.common.MultiSelectionStyle
import kanti.tododer.ui.common.TodoDataWithGroup
import kanti.tododer.ui.components.ContentSwitcher
import kanti.tododer.ui.components.ScreenBottomCaption
import kanti.tododer.ui.components.TodoFloatingActionButton
import kanti.tododer.ui.components.dialogs.CreateDialog
import kanti.tododer.ui.components.dialogs.DeleteDialog
import kanti.tododer.ui.components.dialogs.RenameDialog
import kanti.tododer.ui.components.dialogs.SetGroupDialog
import kanti.tododer.ui.components.dialogs.UngroupDialog
import kanti.tododer.ui.components.todo.TodoData
import kanti.tododer.ui.components.todo.TodoEditor
import kanti.tododer.ui.components.todo.TodoEditorControllers
import kanti.tododer.ui.components.todo.TodoEditorDefaults
import kanti.tododer.ui.components.todoGroupPanel
import kanti.tododer.ui.screen.todo_detail.viewmodel.TodoDetailViewModel
import kanti.tododer.ui.screen.todo_detail.viewmodel.TodoDetailViewModelImpl
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailTopBar(
	multiSelection: Boolean,
	navIconClick: () -> Unit,
	title: String,
	scrollBehavior: TopAppBarScrollBehavior,
	editorSize: IntSize,
	state: TodoData,
	enabledDeleting: Boolean,
	onDoneChange: (isDone: Boolean) -> Unit,
	onMultiSelection: () -> Unit,
//	onArchive: () -> Unit,
	onDelete: () -> Unit
) {
	TopAppBar(
		title = {
			val (offsetYFraction, alphaFraction) = if (scrollBehavior.state.overlappedFraction > 0.01f)
				Pair(0f, 1f) else Pair(1f, 0f)
			val offsetYAnim by animateFloatAsState(
				targetValue = offsetYFraction * 64,
				animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
				label = ""
			)
			val alphaAnim by animateFloatAsState(
				targetValue = alphaFraction,
				animationSpec = spring(stiffness = Spring.StiffnessMedium),
				label = ""
			)
			Text(
				modifier = Modifier
					.offset(y = offsetYAnim.dp)
					.alpha(alpha = alphaAnim),
				text = title,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)
		},
		navigationIcon = {
			IconButton(onClick = navIconClick) {
				ContentSwitcher(
					state = multiSelection,
					trueContent = {
						Icon(
							imageVector = Icons.Default.Clear,
							contentDescription = null
						)
					}, falseContent = {
						Icon(
							imageVector = Icons.Default.ArrowBack,
							contentDescription = null
						)
					}
				)
			}
		},
		actions = {
			val (offsetYFraction, alphaFraction) = with(LocalDensity.current) {
				if (scrollBehavior.state.contentOffset <= -(editorSize.height - 14.dp.toPx()))
					Pair(0f, 1f) else Pair(1f, 0f)
			}
			val offsetYAnim by animateFloatAsState(
				targetValue = offsetYFraction * 64,
				animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
				label = "offsetYAnim"
			)
			val alphaAnim by animateFloatAsState(
				targetValue = alphaFraction,
				animationSpec = spring(stiffness = Spring.StiffnessMedium),
				label = "alphaAnim"
			)
			TodoEditorControllers(
				modifier = Modifier
					.offset(y = offsetYAnim.dp)
					.alpha(alpha = alphaAnim),
				state = state,
				enabledDeleting = enabledDeleting,
				onDoneChanged = onDoneChange,
				preAction = { PreAction(onMultiSelection = onMultiSelection) },
//				onArchive = onArchive,
				onDelete = onDelete
			)
		},
		scrollBehavior = scrollBehavior
	)
}

@Composable
private fun PreAction(
	onMultiSelection: () -> Unit,
) {
	IconButton(onClick = onMultiSelection) {
		Icon(
			painter = painterResource(id = R.drawable.multi_select),
			contentDescription = null
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TodoDetailScreen(
	navController: NavController = rememberNavController(),
	selectionStyle: Set<MultiSelectionStyle> = setOf(MultiSelectionStyle.ColorFill),
	vm: TodoDetailViewModel = hiltViewModel<TodoDetailViewModelImpl>(),
	todoId: Long = 0
) {
	val logTag = "TodoDetailScreen"

	var showCreateDialog by rememberSaveable { mutableStateOf(false) }
	var showSetGroupDialog: List<TodoDataWithGroup>? by rememberSaveable {
		mutableStateOf(null)
	}
	var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
	var showRenameDialog: TodoData? by rememberSaveable { mutableStateOf(null) }
	var showUngroupDialog: String? by rememberSaveable { mutableStateOf(null) }

	LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
		vm.show(todoId)
	}

	LifecycleResumeEffect {
		vm.reshow(null)

		onPauseOrDispose { }
	}

	LifecycleStartEffect(key1 = vm) {
		val deletedTodo = navController.currentBackStackEntry?.savedStateHandle
			?.get<Long>(UiConst.BackStackKeys.DELETED)
		vm.reshow(deletedTodo)

		navController.currentBackStackEntry?.savedStateHandle
			?.set(UiConst.BackStackKeys.DELETED, null)
		onStopOrDispose {
			Log.d(logTag, "onStop($todoId): vm.stop()")
			vm.onStop()
		}
	}

	LaunchedEffect(key1 = vm) {
		vm.onExit.collectLatest { todoData ->
			navController.previousBackStackEntry?.savedStateHandle
				?.set(UiConst.BackStackKeys.DELETED, todoData?.id)
			navController.popBackStack()
		}
	}

	val todoChildrenRoute = stringResource(id = R.string.nav_destination_todo_detail)
	fun todoDetailRoute(todoId: Long): String {
		return "$todoChildrenRoute/$todoId"
	}
	LaunchedEffect(key1 = vm) {
		vm.toNext.collectLatest { todoId ->
			navController.navigate(
				route = todoDetailRoute(todoId)
			)
		}
	}

	val context = LocalContext.current
	val snackBarHost = remember { SnackbarHostState() }
	LaunchedEffect(key1 = vm) {
		vm.childrenTodosDeleted.collectLatest { todos ->
			if (todos.isEmpty())
				return@collectLatest

			val size = todos.size
			val soloTodo = size == 1
			val message = if (soloTodo) {
				val regexName = context.getString(R.string.regex_name)
				val todoDeleted = context.getString(R.string.todo_deleted)
				todoDeleted.replace(regexName, todos[0].todoData.title)
			} else
				context.resources.getQuantityString(R.plurals.todos_deleted, size, size)

			val result = snackBarHost.showSnackbar(
				message = message,
				actionLabel = context.getString(R.string.cancel),
				duration = SnackbarDuration.Short
			)
			when (result) {
				SnackbarResult.ActionPerformed -> {
					vm.cancelDeleteChildren()
					if (soloTodo && todos[0].returnToChild) {
						navController.navigate(
							route = todoDetailRoute(todos[0].todoData.id)
						)
					}
				}

				else -> vm.rejectCancelDelete()
			}
		}
	}

	val blankTodoDeleted = stringResource(id = R.string.deleted_blank_todo)
	LaunchedEffect(key1 = vm) {
		vm.blankTodoDeleted.collectLatest {
			snackBarHost.showSnackbar(
				message = blankTodoDeleted
			)
		}
	}

	LaunchedEffect(key1 = vm) {
		vm.groupSelected.collectLatest { showSetGroupDialog = it }
	}

	val todoDetail by vm.todoDetail.collectAsState()
	val todoChildren by vm.todoChildren.collectAsState()

	if (todoChildren.selection) {
		BackHandler {
			vm.selectionOff()
		}
	}

	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
	var editorSize by remember {
		mutableStateOf(IntSize(0, 400))
	}

	val onMultiSelection = { vm.switchSelection() }
	val onDelete = { showDeleteDialog = true }

	Scaffold(
		modifier = Modifier
			.nestedScroll(scrollBehavior.nestedScrollConnection),

		topBar = {
			TodoDetailTopBar(
				multiSelection = todoChildren.selection,
				navIconClick = {
					if (todoChildren.selection)
						vm.selectionOff()
					else
						navController.popBackStack()
				},
				title = todoDetail.title,
				scrollBehavior = scrollBehavior,
				editorSize = editorSize,
				state = todoDetail,
				enabledDeleting = !todoChildren.selection,
				onDoneChange = {
					vm.changeDoneCurrent(it)
				},
//				onArchive = {},
				onMultiSelection = onMultiSelection,
				onDelete = onDelete
			)
		},

		snackbarHost = {
			SnackbarHost(hostState = snackBarHost) { snackbarData ->
				val dismissState = rememberDismissState(
					confirmValueChange = {
						snackbarData.dismiss()
						true
					}
				)
				SwipeToDismiss(
					state = dismissState,
					background = {},
					dismissContent = { Snackbar(snackbarData = snackbarData) }
				)
			}
		},

		floatingActionButton = {
			TodoFloatingActionButton(
				selection = todoChildren.selection,
				onClick = { showCreateDialog = true },
				onGroup = { vm.groupSelected() },
				onCheck = { vm.changeDoneSelected() },
				onDelete = { vm.deleteSelected() }
			)
		}
	) { paddingValues ->
		LazyColumn(
			modifier = Modifier
				.padding(paddingValues)
		) {
			item {
				TodoEditor(
					modifier = Modifier
						.fillMaxWidth()
						.onSizeChanged { size ->
							editorSize = size
						},
					initialState = todoDetail,
					strings = TodoEditorDefaults.strings(
						title = stringResource(id = R.string.title),
						remark = stringResource(id = R.string.remark)
					),
					enabledDeleting = !todoChildren.selection,
					onTitleChanged = { title ->
						vm.changeTitle(title)
					},
					onRemarkChanged = { remark ->
						vm.changeRemark(remark)
					},
					onDoneChanged = { isDone ->
						vm.changeDoneCurrent(isDone)
					},
					preAction = { PreAction(onMultiSelection) },
//					onArchive = {},
					onDelete = onDelete
				)

				Divider(
					modifier = Modifier
						.padding(
							top = 8.dp,
							bottom = 8.dp,
							start = 16.dp,
							end = 16.dp
						)
				)

				Spacer(
					modifier = Modifier.height(12.dp)
				)
			}

			for (index in 0..<todoChildren.groups.size) {
				todoGroupPanel(
					selection = todoChildren.selection,
					isSingleGroup = todoChildren.groups.size == 1,
					group = todoChildren.groups[index],
					isFirstGroup = index == 0,
					allowGrouping = true,
					selectionStyle = selectionStyle,
					groupOnChangeExpand = { group, expand ->
						vm.setGroupExpand(group, expand)
					},
					groupOnChangeSelect = { group, selected ->
						vm.setSelect(group = group, selected = selected)
					},
					groupMenuOnChangeDone = { group, isDone ->
						vm.changeGroupDone(group, isDone)
					},
					groupMenuOnRename = { vm.renameGroup(it) },
					groupMenuOnUngroup = { showUngroupDialog = it },
					groupMenuOnSelect = { vm.selection(it) },
					groupMenuOnDeleteGroup = { vm.deleteGroup(it) },
					itemOnLongClick = { todoData -> vm.selection(todoData.id) },
					itemOnClick = { todoData ->
						navController.navigate(
							route = todoDetailRoute(todoData.id)
						)
					},
					itemOnDoneChange = { todoData, selected ->
						vm.changeDoneChild(todoData.id, selected)
					},
					itemOnChangeSelect = { todoData, selected ->
						vm.setSelect(todoData.id, selected)
					},
					itemMenuOnAddToGroup = { todoData ->
						showSetGroupDialog = listOf(
							TodoDataWithGroup(
								todoData = todoData,
								group = todoChildren.groups[index].name
							)
						)
					},
					itemMenuOnRename = { todoData -> showRenameDialog = todoData },
					itemMenuOnSelect = { todoData -> vm.selection(todoData.id) },
					itemMenuOnDelete = { todoData -> vm.deleteChildren(listOf(todoData)) }
				)
			}

			item {
				Column(
					modifier = Modifier.animateItemPlacement()
				) {
					Spacer(modifier = Modifier.height(height = 8.dp))
					val captionStg = stringResource(id = R.string.caption_todo_detail)
					val allTodos = todoChildren.groups.asSequence().flatMap { it.todos }
					ScreenBottomCaption(
						modifier = Modifier
							.fillMaxWidth()
							.padding(bottom = 16.dp)
							.height(56.dp),
						text = captionStg
							.replace("{1}", todoDetail.title)
							.replace(
								"{2}",
								allTodos.filter { it.visible }.count().toString()
							)
							.replace(
								"{3}",
								allTodos.filter { it.data.isDone && it.visible }.count().toString()
							)
					)
				}
			}
		}
	}

	if (showCreateDialog) {
		CreateDialog(
			onCloseDialog = { showCreateDialog = false },
			title = { Text(text = stringResource(id = R.string.create_new_todo)) },
			textFieldLabel = { Text(text = stringResource(id = R.string.todo_name)) },
			add = { title ->
				vm.createNewTodo(title = title, goTo = false)
			},
			create = { title ->
				vm.createNewTodo(title = title, goTo = false)
			}
		)
	}

	if (showSetGroupDialog != null) {
		val groupingTodos = showSetGroupDialog!!.asSequence()
		val initialGroup = groupingTodos.map { it.group }
			.reduce { acc, group -> if (acc == group) acc else null }
		SetGroupDialog(
			onDismissRequest = { showSetGroupDialog = null },
			initialGroup = initialGroup,
			groups = todoChildren.groups.map { it.name }.toSet(),
			onSetGroup = { groupName ->
				vm.setGroup(groupingTodos.map { it.todoData.id }.toList(), groupName)
				vm.selectionOff()
			}
		)
	}

	if (showDeleteDialog) {
		DeleteDialog(
			onCloseDialog = { showDeleteDialog = false },
			title = { Text(text = stringResource(id = R.string.delete_current_todo_ask)) },
			delete = { vm.deleteCurrent() }
		)
	}

	if (showRenameDialog != null) {
		val renamedTodo = showRenameDialog!!
		RenameDialog(
			onCloseDialog = { showRenameDialog = null },
			title = { Text(text = stringResource(id = R.string.rename_todo)) },
			label = { Text(text = stringResource(id = R.string.new_title)) },
			name = renamedTodo.title,
			onRename = { newTitle ->
				vm.renameTodo(renamedTodo.id, newTitle)
			}
		)
	}

	if (showUngroupDialog != null) {
		val groupName = showUngroupDialog!!
		UngroupDialog(
			onDismissRequest = { showUngroupDialog = null },
			group = groupName,
			onUngroup = { vm.ungroup(groupName) }
		)
	}
}

@Preview
@Composable
private fun PreviewTodoDetailScreen() {
	TodoDetailScreen(
		vm = TodoDetailViewModel
	)
}