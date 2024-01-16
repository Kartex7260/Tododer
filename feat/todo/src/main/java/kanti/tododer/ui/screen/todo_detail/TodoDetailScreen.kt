package kanti.tododer.ui.screen.todo_detail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.components.dialogs.DeleteTodoDialog
import kanti.tododer.ui.components.menu.NormalTodoDropdownMenu
import kanti.tododer.ui.components.todo.TodoCard
import kanti.tododer.ui.components.todo.TodoEditor
import kanti.tododer.ui.components.todo.TodoEditorControllers
import kanti.tododer.ui.components.todo.TodoData
import kanti.tododer.ui.screen.todo_detail.viewmodel.TodoDetailViewModel
import kanti.tododer.ui.screen.todo_detail.viewmodel.TodoDetailViewModelImpl
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailTopBar(
	back: () -> Unit,
	title: String,
	scrollBehavior: TopAppBarScrollBehavior,
	editorSize: IntSize,
	state: TodoData,
	onDoneChange: (isDone: Boolean) -> Unit,
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
			IconButton(onClick = { back() }) {
				Icon(
					imageVector = Icons.Default.ArrowBack,
					contentDescription = null
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
				label = ""
			)
			val alphaAnim by animateFloatAsState(
				targetValue = alphaFraction,
				animationSpec = spring(stiffness = Spring.StiffnessMedium),
				label = ""
			)
			TodoEditorControllers(
				modifier = Modifier
					.offset(y = offsetYAnim.dp)
					.alpha(alpha = alphaAnim),
				state = state,
				onDoneChanged = onDoneChange,
//				onArchive = onArchive,
				onDelete = onDelete
			)
		},
		scrollBehavior = scrollBehavior
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(
	navController: NavController = rememberNavController(),
	vm: TodoDetailViewModel = hiltViewModel<TodoDetailViewModelImpl>(),
	todoId: Long = 0
) {
	LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
		vm.push(todoId)
	}

	LaunchedEffect(key1 = vm) {
		vm.emptyStack.collect {
			navController.popBackStack()
		}
	}

	val snackBarHost = remember { SnackbarHostState() }
	val soloDeletedFragment1 = stringResource(id = R.string.deleted_solo_1)
	val soloDeletedFragment2 = stringResource(id = R.string.deleted_solo_2_todo)
	val multiDeletedFragment1 = stringResource(id = R.string.deleted_multi_1)
	val multiDeletedFragment2 = stringResource(id = R.string.deleted_multi_2_todo)
	val cancelStringRes = stringResource(id = R.string.cancel)
	LaunchedEffect(key1 = vm) {
		vm.childrenTodosDeleted.collectLatest { todos ->
			val message = if (todos.size == 1) {
				val todo = todos[0]
				"$soloDeletedFragment1 \"${todo.title}\" $soloDeletedFragment2"
			} else {
				"$multiDeletedFragment1 ${todos.size} $multiDeletedFragment2"
			}
			val result = snackBarHost.showSnackbar(
				message = message,
				withDismissAction = true,
				actionLabel = cancelStringRes,
				duration = SnackbarDuration.Short
			)
			when (result) {
				SnackbarResult.ActionPerformed -> {
					vm.cancelDeleteChildren()
				}
				else -> {
					vm.rejectCancelDelete()
				}
			}
		}
	}

	LaunchedEffect(key1 = vm) {
		vm.currentTodoDeleted.collectLatest { todo ->
			val message = "$soloDeletedFragment1 \"${todo.title}\" $soloDeletedFragment2"
			val result = snackBarHost.showSnackbar(
				message = message,
				withDismissAction = true,
				actionLabel = cancelStringRes,
				duration = SnackbarDuration.Short
			)
			when (result) {
				SnackbarResult.ActionPerformed -> {
					vm.cancelDeleteCurrent()
				}
				else -> {
					vm.rejectCancelDelete()
				}
			}
		}
	}

	LifecycleStartEffect(key1 = vm) {
		onStopOrDispose {
			vm.rejectCancelDelete()
		}
	}

	val todoDetail by vm.todoDetail.collectAsState()
	val todoChildren by vm.todoChildren.collectAsState()

	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
	var editorSize by remember {
		mutableStateOf(IntSize(0, 0))
	}

	var showDeleteDialog by remember {
		mutableStateOf(false)
	}

	Scaffold(
		modifier = Modifier
			.nestedScroll(scrollBehavior.nestedScrollConnection),

		topBar = {
			TodoDetailTopBar(
				back = { vm.pop() },
				title = todoDetail.title,
				scrollBehavior = scrollBehavior,
				editorSize = editorSize,
				state = todoDetail,
				onDoneChange = {
					vm.changeDoneCurrent(it)
				},
//				onArchive = {},
				onDelete = {
					showDeleteDialog = true
				}
			)
		},

		snackbarHost = {
			SnackbarHost(hostState = snackBarHost)
		},

		floatingActionButton = {
			FloatingActionButton(
				onClick = { vm.createNewTodo() },
				containerColor = MaterialTheme.colorScheme.tertiaryContainer,
				contentColor = MaterialTheme.colorScheme.onTertiaryContainer
			) {
				Icon(
					imageVector = Icons.Default.Add,
					contentDescription = null
				)
			}
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
					onTitleChanged = { title ->
						vm.changeTitle(title)
					},
					onRemarkChanged = { remark ->
						vm.changeRemark(remark)
					},
					onDoneChanged = { isDone ->
						vm.changeDoneCurrent(isDone)
					},
//					onArchive = {},
					onDelete = {
						showDeleteDialog = true
					}
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
			}

			items(
				items = todoChildren.todos,
				key = { it.id }
			) { todoData ->
				TodoCard(
					modifier = Modifier
						.padding(
							top = 16.dp,
							start = 16.dp,
							end = 16.dp
						),
					todoData = todoData,
					onDoneChange = { isDone ->
						vm.changeDoneChild(todoData.id, isDone)
					},
					onClick = {
						vm.push(todoData.id)
					}
				) {
					var expanded by remember {
						mutableStateOf(false)
					}
					IconButton(
						onClick = { expanded = true }
					) {
						Icon(
							imageVector = Icons.Default.MoreVert,
							contentDescription = null
						)
					}
					NormalTodoDropdownMenu(
						expanded = expanded,
						onDismissRequest = { expanded = false },
						onDelete = { vm.deleteChildren(listOf(todoData)) }
					)
				}
			}
		}
	}

	if (showDeleteDialog) {
		DeleteTodoDialog(
			onCloseDialog = { showDeleteDialog = false },
			todoTitle = todoDetail.title,
			delete = { vm.deleteCurrent() }
		)
	}

	BackHandler {
		vm.pop()
	}
}

@Preview
@Composable
private fun PreviewTodoDetailScreen() {
	TodoDetailScreen(
		vm = TodoDetailViewModel
	)
}