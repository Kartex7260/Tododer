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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.ui.components.todo.TodoLazyColumn
import kanti.tododer.ui.components.todo.TodosUiState
import kanti.tododer.ui.screen.todo_list.viewmodel.TodoListUiState
import kanti.tododer.ui.screen.todo_list.viewmodel.TodoListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TodoListTopBar(
	plan: Plan?,
	scrollBehavior: TopAppBarScrollBehavior,
	navigationIcon: (@Composable () -> Unit)? = null,
	topBarActions: (@Composable () -> Unit)?
) {
	val title = when (plan?.type) {
		PlanType.All -> stringResource(id = R.string.plan_all)
		PlanType.Default -> stringResource(id = R.string.plan_default)
		PlanType.Custom -> plan.title
		else -> ""
	}
	CenterAlignedTopAppBar(
		title = {
			Text(text = title)
		},
		navigationIcon = {
			if (navigationIcon != null) {
				navigationIcon()
			}
		},
		actions = {
			if (topBarActions != null) {
				topBarActions()
			}
		},
		scrollBehavior = scrollBehavior
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
	navController: NavController,
	navigationIcon: (@Composable () -> Unit)? = null,
	topBarActions: (@Composable () -> Unit)? = null,
	vm: TodoListViewModel = TodoListViewModel
) {
	val todoListUiState by vm.currentPlan.collectAsState()
	val (plan, children) = when (todoListUiState) {
		is TodoListUiState.Empty -> Pair(null, null)
		is TodoListUiState.Success -> {
			Pair(
				(todoListUiState as TodoListUiState.Success).plan,
				(todoListUiState as TodoListUiState.Success).children
			)
		}
		is TodoListUiState.Fail -> {
			Pair(null, null)
		}
	}

	val todoChildrenRoute = stringResource(id = R.string.nav_destination_todo_detail)
	val todoChildrenRouteTodoIdParam = stringResource(
		id = R.string.nav_destination_todo_detail_todo_id_param
	)

	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
	Scaffold(
		modifier = Modifier
			.nestedScroll(scrollBehavior.nestedScrollConnection),

		topBar = {
			TodoListTopBar(
				plan = plan,
				scrollBehavior = scrollBehavior,
				navigationIcon = navigationIcon,
				topBarActions = topBarActions
			)
		},
		floatingActionButton = {
			FloatingActionButton(
				onClick = {
					navController.navigate(
						route = todoChildrenRoute
					)
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
			content = children ?: TodosUiState(),
			onClick = {
				navController.navigate(
					route = "$todoChildrenRoute?" +
							"$todoChildrenRouteTodoIdParam=${it.id}"
				)
			},
			onDoneChanged = { _, _ -> }
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
		navigationIcon = {
			IconButton(onClick = {  }) {
				Icon(
					imageVector = Icons.Default.List,
					contentDescription = null
				)
			}
		},
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