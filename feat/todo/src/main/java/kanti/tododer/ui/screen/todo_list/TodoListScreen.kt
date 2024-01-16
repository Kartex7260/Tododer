package kanti.tododer.ui.screen.todo_list

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kanti.tododer.Const
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.components.menu.NormalTodoDropdownMenu
import kanti.tododer.ui.components.todo.TodoLazyColumn
import kanti.tododer.ui.components.todo.TodosData
import kanti.tododer.ui.screen.todo_list.viewmodel.TodoListUiState
import kanti.tododer.ui.screen.todo_list.viewmodel.TodoListViewModel
import kanti.tododer.ui.screen.todo_list.viewmodel.TodoListViewModelImpl
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TodoListTopBar(
	plan: Plan?,
	navController: NavController,
	scrollBehavior: TopAppBarScrollBehavior,
	topBarActions: @Composable () -> Unit
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
		actions = { topBarActions() },
		scrollBehavior = scrollBehavior
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
	navController: NavController,
	topBarActions: @Composable () -> Unit = {},
	vm: TodoListViewModel = hiltViewModel<TodoListViewModelImpl>()
) {
	val snackbarHostState = remember {
		SnackbarHostState()
	}

	val todoListUiState by vm.currentPlan.collectAsState()
	val (plan, children) = when (todoListUiState) {
		is TodoListUiState.Empty -> Pair(Plan(), TodosData())
		is TodoListUiState.Success -> {
			Pair(
				(todoListUiState as TodoListUiState.Success).plan,
				(todoListUiState as TodoListUiState.Success).children
			)
		}
		is TodoListUiState.Fail -> {
			Pair(Plan(), TodosData())
		}
	}

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
			vm.rejectCancelChance()
		}
	}

	LifecycleResumeEffect(key1 = vm) {
		val deletedTodoId = navController.currentBackStackEntry?.savedStateHandle
			?.get<Long>(Const.BackStackKeys.DELETED)
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

	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
	Scaffold(
		modifier = Modifier
			.nestedScroll(scrollBehavior.nestedScrollConnection),

		topBar = {
			TodoListTopBar(
				plan = plan,
				scrollBehavior = scrollBehavior,
				navController = navController,
				topBarActions = topBarActions
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
					onDelete = {
						vm.deleteTodos(listOf(todoData))
					}
				)
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
		topBarActions = {
			IconButton(onClick = {  }) {
				Icon(
					imageVector = Icons.Default.MoreVert,
					contentDescription = null
				)
			}
		}
	)
}