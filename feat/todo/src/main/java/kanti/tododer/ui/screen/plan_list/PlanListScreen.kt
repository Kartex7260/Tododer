package kanti.tododer.ui.screen.plan_list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.components.menu.NormalPlanDropdownMenu
import kanti.tododer.ui.components.plan.PlanCard
import kanti.tododer.ui.components.plan.PlanLazyColumn
import kanti.tododer.ui.screen.plan_list.viewmodel.PlanListViewModel
import kanti.tododer.ui.screen.plan_list.viewmodel.PlanListViewModelImpl
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlanListTopBar(
	back: () -> Unit,
	topBarActions: @Composable () -> Unit,
	scrollBehavior: TopAppBarScrollBehavior
) {
	CenterAlignedTopAppBar(
		title = {
			Text(text = stringResource(id = R.string.plans))
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
			topBarActions()
		},
		scrollBehavior = scrollBehavior
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanListScreen(
	navController: NavController = rememberNavController(),
	topBarActions: @Composable () -> Unit = {},
	vm: PlanListViewModel = hiltViewModel<PlanListViewModelImpl>()
) {
	val onBack = {
		vm.undoChanceRejected()
		navController.popBackStack()
	}

	BackHandler {
		onBack()
	}

	var showDialog by rememberSaveable { mutableStateOf(false) }
	val snackbarHostState = remember { SnackbarHostState() }

	val planAll by vm.planAll.collectAsState()
	val planDefault by vm.planDefault.collectAsState()
	val plans by vm.plans.collectAsState()

	LaunchedEffect(key1 = vm) {
		vm.newPlanCreated.collect {
			onBack()
		}
	}

	val deletedFragment1 = stringResource(id = R.string.deleted_1)
	val deletedFragment2 = stringResource(id = R.string.deleted_2_plan)
	val cancelStringRes = stringResource(id = R.string.cancel)
	LaunchedEffect(key1 = vm) {
		vm.planDeleted.collectLatest { planTitle ->
			val result = snackbarHostState.showSnackbar(
				message = "$deletedFragment1 \"$planTitle\" $deletedFragment2",
				actionLabel = cancelStringRes,
				withDismissAction = true,
				duration = SnackbarDuration.Short
			)
			when (result) {
				SnackbarResult.ActionPerformed -> {
					vm.undoDelete()
				}
				SnackbarResult.Dismissed -> {
					vm.undoChanceRejected()
				}
			}
		}
	}

	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
	Scaffold(
		modifier = Modifier
			.nestedScroll(scrollBehavior.nestedScrollConnection),

		topBar = {
			PlanListTopBar(
				back = { onBack() },
				topBarActions = topBarActions,
				scrollBehavior = scrollBehavior
			)
		},

		snackbarHost = {
			SnackbarHost(hostState = snackbarHostState) { snackbarData ->
				Snackbar(snackbarData = snackbarData)
			}
		},

		floatingActionButton = {
			FloatingActionButton(
				onClick = {
					showDialog = true
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
		PlanLazyColumn(
			modifier = Modifier
				.padding(paddingValues),
			preContent = {
				PlanCard(
					planData = planAll,
					onClick = {
						vm.setCurrentPlan(planAll.id)
						onBack()
					}
				)

				PlanCard(
					modifier = Modifier
						.padding(top = 16.dp),
					planData = planDefault,
					onClick = {
						vm.setCurrentPlan(planDefault.id)
						onBack()
					}
				)

				Divider(
					modifier = Modifier
						.padding(
							top = 16.dp,
							bottom = 16.dp,
							start = 16.dp,
							end = 16.dp
						)
				)
			},
			content = plans,
			onClick = { plan ->
				vm.setCurrentPlan(plan.id)
				onBack()
			},
			action = { planUiState ->
				var showDropdownMenu by remember {
					mutableStateOf(false)
				}
				IconButton(onClick = { showDropdownMenu = true }) {
					Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
				}
				NormalPlanDropdownMenu(
					expanded = showDropdownMenu,
					onDismissRequest = { showDropdownMenu = false },
					onDelete = {
						vm.deletePlan(planUiState.id)
					}
				)
			}
		)
	}

	if (showDialog) {
		CreatePlanDialog(
			onCloseDialog = {
				showDialog = false
			},
			create = { title ->
				vm.createPlanEndSetCurrent(title)
			}
		)
	}
}

@Preview
@Composable
private fun PreviewPlanListScreen() {
	PlanListScreen(
		topBarActions = {
			IconButton(onClick = { }) {
				Icon(
					imageVector = Icons.Default.MoreVert,
					contentDescription = null
				)
			}
		},
		vm = PlanListViewModel
	)
}