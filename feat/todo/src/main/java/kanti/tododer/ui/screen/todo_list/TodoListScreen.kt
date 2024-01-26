package kanti.tododer.ui.screen.todo_list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.UiConst
import kanti.tododer.ui.components.dialogs.RenameDialog
import kanti.tododer.ui.components.menu.NormalTodoDropdownMenu
import kanti.tododer.ui.components.todo.TodoData
import kanti.tododer.ui.components.todo.TodoLazyColumn
import kanti.tododer.ui.screen.todo_list.viewmodel.TodoListViewModel
import kanti.tododer.ui.screen.todo_list.viewmodel.TodoListViewModelImpl
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TodoListTopBar(
	plan: Plan?,
	navController: NavController,
	scrollBehavior: TopAppBarScrollBehavior,
	optionMenuItems: (@Composable (closeMenu: () -> Unit) -> Unit)?
) {
	val title = when (plan?.type) {
		PlanType.All -> stringResource(id = R.string.plan_all)
		PlanType.Default -> stringResource(id = R.string.plan_default)
		PlanType.Custom -> plan.title
		else -> ""
	}
	val plansRoute = stringResource(id = R.string.nav_destination_plans)
	
	CenterAlignedTopAppBar(
		title = {
			Text(text = title)
		},
		navigationIcon = {
			IconButton(onClick = {
				navController.navigate(plansRoute)
			}) {
				Icon(
					imageVector = Icons.Default.List,
					contentDescription = null
				)
			}
		},
		actions = {
			if (optionMenuItems != null) {
				var expandOptionMenu by rememberSaveable { mutableStateOf(false) }
				IconButton(onClick = { expandOptionMenu = true }) {
					Icon(
						imageVector = Icons.Default.MoreVert,
						contentDescription = null,
					)
				}
				DropdownMenu(
					expanded = expandOptionMenu,
					onDismissRequest = { expandOptionMenu = false },
					properties = PopupProperties()
				) {
					optionMenuItems {
						expandOptionMenu = false
					}
				}
			}
		},
		scrollBehavior = scrollBehavior
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
	navController: NavController,
	optionMenuItems: (@Composable (closeMenu: () -> Unit) -> Unit)? = null,
	vm: TodoListViewModel = hiltViewModel<TodoListViewModelImpl>()
) {
	var showRenameDialog: TodoData? by rememberSaveable { mutableStateOf(null) }

	val snackbarHostState = remember {
		SnackbarHostState()
	}

	val todoListUiState by vm.currentPlan.collectAsState()
	val plan = todoListUiState.plan
	val children = todoListUiState.children

	val soloDeletedFragment1 = stringResource(id = R.string.deleted_solo_1)
	val soloDeletedFragment2 = stringResource(id = R.string.deleted_solo_2_todo)
	val multiDeletedFragment1 = stringResource(id = R.string.deleted_multi_1)
	val multiDeletedFragment2 = stringResource(id = R.string.deleted_multi_2_todo)
	val cancelStringRes = stringResource(id = R.string.cancel)
	LaunchedEffect(key1 = vm) {
		vm.todosDeleted.collectLatest { deletedTodos ->
			val message = if (deletedTodos.size == 1) {
				val deletedTodo = deletedTodos[0]
				"$soloDeletedFragment1 \"${deletedTodo.title}\" $soloDeletedFragment2"
			} else {
				"$multiDeletedFragment1 ${deletedTodos.size} $multiDeletedFragment2"
			}
			val result = snackbarHostState.showSnackbar(
				message = message,
				actionLabel = cancelStringRes,
				withDismissAction = true,
				duration = SnackbarDuration.Short
			)
			when (result) {
				SnackbarResult.ActionPerformed -> {
					vm.cancelDelete()
				}
				else -> {
					vm.rejectCancelChance()
				}
			}
		}
	}

	LifecycleStartEffect(key1 = vm) {
		onStopOrDispose {
			vm.onStop()
		}
	}

	LifecycleResumeEffect(key1 = vm) {
		val deletedTodoId = navController.currentBackStackEntry?.savedStateHandle
			?.get<Long>(UiConst.BackStackKeys.DELETED)
		vm.updateUiState(deletedTodoId)
		onPauseOrDispose {  }
	}

	val todoChildrenRoute = stringResource(id = R.string.nav_destination_todo_detail)
	fun todoDetailRoute(todoId: Long): String {
		return "$todoChildrenRoute/$todoId"
	}

	LaunchedEffect(key1 = vm) {
		vm.newTodoCreated.collect { todoId ->
			navController.navigate(
				route = todoDetailRoute(todoId)
			)
		}
	}

	@Composable
	fun <T : Any> rememberSaveableByPlan(
		saver: Saver<T, out Any> = autoSaver(),
		init: () -> T
	): T {
		return rememberSaveable(
			inputs = arrayOf(plan),
			saver = saver,
			key = plan.id.toString(),
			init = init
		)
	}

	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
		state = rememberSaveableByPlan(saver = TopAppBarState.Saver) {
			TopAppBarState(
				initialHeightOffsetLimit = -Float.MAX_VALUE,
				initialHeightOffset = 0f,
				initialContentOffset = 0f
			)
		}
	)
	Scaffold(
		modifier = Modifier
			.nestedScroll(scrollBehavior.nestedScrollConnection),

		topBar = {
			TodoListTopBar(
				plan = plan,
				scrollBehavior = scrollBehavior,
				navController = navController,
				optionMenuItems = optionMenuItems
			)
		},

		snackbarHost = {
			SnackbarHost(hostState = snackbarHostState)
		},

		floatingActionButton = {
			FloatingActionButton(
				onClick = {
					vm.createNewTodo()
				},
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
		TodoLazyColumn(
			modifier = Modifier.padding(paddingValues),
			state = rememberSaveableByPlan(saver = LazyListState.Saver) {
				LazyListState(0, 0)
			},
			content = children,
			onClick = { todoData ->
				navController.navigate(
					route = todoDetailRoute(todoData.id)
				)
			},
			onDoneChanged = { isDone, todo ->
				vm.changeDone(todo.id, isDone)
			},
			actions = { todoData ->
				var showDropdownMenu by remember {
					mutableStateOf(false)
				}
				IconButton(onClick = { showDropdownMenu = true }) {
					Icon(
						imageVector = Icons.Default.MoreVert,
						contentDescription = null
					)
				}
				NormalTodoDropdownMenu(
					expanded = showDropdownMenu,
					onDismissRequest = { showDropdownMenu = false },
					onRename = {
						showRenameDialog = todoData
					},
					onDelete = {
						vm.deleteTodos(listOf(todoData))
					}
				)
			}
		)
	}

	if (showRenameDialog != null) {
		val renamedTodo = showRenameDialog!!
		RenameDialog(
			onCloseDialog = {
				showRenameDialog = null
			},
			label = { Text(text = stringResource(id = R.string.new_title))},
			name = renamedTodo.title,
			onRename = { newTitle ->
				vm.renameTodo(renamedTodo.id, newTitle)
			}
		)
	}
}

@Preview(
	showBackground = true
)
@Composable
fun PreviewTodoListScreen() {
	TodoListScreen(
		navController = rememberNavController(),
		vm = TodoListViewModel,
		optionMenuItems = { closeMenu ->
			DropdownMenuItem(
				text = { Text(text = "Test menu item") },
				onClick = { closeMenu() }
			)
		}
	)
}