package kanti.tododer.ui.screen.plan_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kanti.tododer.data.model.progress.PlanProgress
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.components.dialogs.RenameDialog
import kanti.tododer.ui.components.menu.NormalPlanDropdownMenu
import kanti.tododer.ui.components.plan.PlanCard
import kanti.tododer.ui.components.plan.PlanData
import kanti.tododer.ui.screen.plan_list.viewmodel.PlanListViewModel
import kanti.tododer.ui.screen.plan_list.viewmodel.PlanListViewModelImpl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlanListTopBar(
	back: () -> Unit,
	optionMenuItems: (@Composable (closeMenu: () -> Unit) -> Unit)?,
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
			if (optionMenuItems != null) {
				var expandOptionMenu by rememberSaveable { mutableStateOf(false) }
				IconButton(onClick = { expandOptionMenu = true }) {
					Icon(
						imageVector = Icons.Default.MoreVert,
						contentDescription = null
					)
				}
				DropdownMenu(
					expanded = expandOptionMenu,
					onDismissRequest = { expandOptionMenu = false }
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
fun PlanListScreen(
	navController: NavController = rememberNavController(),
	optionMenuItems: (@Composable (closeMenu: () -> Unit) -> Unit)? = null,
	vm: PlanListViewModel = hiltViewModel<PlanListViewModelImpl>()
) {
	var showRenameDialog: PlanData? by rememberSaveable { mutableStateOf(null) }

	var showCreateDialog by rememberSaveable { mutableStateOf(false) }
	val snackbarHostState = remember { SnackbarHostState() }

	val cancelStringRes = stringResource(id = R.string.cancel)

	val deletedSoloFragment1 = stringResource(id = R.string.deleted_solo_1)
	val deletedSoloFragment2 = stringResource(id = R.string.deleted_solo_2_plan)
	val deletedMultiFragment1 = stringResource(id = R.string.deleted_multi_1)
	val deletedMultiFragment2 = stringResource(id = R.string.deleted_multi_2_plan)
	LaunchedEffect(key1 = vm) {
		vm.plansDeleted.collectLatest { plans ->
			if (plans.isNotEmpty()) {
				val message = if (plans.size == 1) {
					val plan = plans[0]
					"$deletedSoloFragment1 \"${plan.title}\" $deletedSoloFragment2"
				} else {
					"$deletedMultiFragment1 ${plans.size} $deletedMultiFragment2"
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
					SnackbarResult.Dismissed -> {
						vm.rejectCancelChance()
					}
				}
			}
		}
	}

	LifecycleResumeEffect(key1 = vm) {
		vm.updateUiState()
		onPauseOrDispose {  }
	}

	LifecycleStartEffect(key1 = vm) {
		onStopOrDispose {
			vm.rejectCancelChance()
		}
	}

	LaunchedEffect(key1 = vm) {
		vm.newPlanCreated.collect {
			navController.popBackStack()
		}
	}

	val planAll by vm.planAll.collectAsState()
	val planDefault by vm.planDefault.collectAsState()
	val plans by vm.plans.collectAsState()

	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
	Scaffold(
		modifier = Modifier
			.nestedScroll(scrollBehavior.nestedScrollConnection),

		topBar = {
			PlanListTopBar(
				back = { navController.popBackStack() },
				optionMenuItems = optionMenuItems,
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
					showCreateDialog = true
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
		LazyColumn(
			modifier = Modifier.padding(paddingValues),
			contentPadding = PaddingValues(
				top = 12.dp,
				bottom = 12.dp,
				start = 16.dp,
				end = 16.dp
			),
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			item {
				val planAllProgress by vm.planAllProgress.collectAsState(initial = 0f)
				val planAllWithProgress = planAll.copy(progress = planAllProgress)
				PlanCard(
					planData = planAllWithProgress,
					onClick = {
						vm.setCurrentPlan(planAll.id)
						navController.popBackStack()
					}
				)

				val planDefaultProgress by vm.planDefaultProgress.collectAsState(initial = 0f)
				val planDefaultWithProgress = planDefault.copy(progress = planDefaultProgress)
				PlanCard(
					modifier = Modifier
						.padding(top = 8.dp),
					planData = planDefaultWithProgress,
					onClick = {
						vm.setCurrentPlan(planDefault.id)
						navController.popBackStack()
					}
				)

				Divider(
					modifier = Modifier
						.padding(
							top = 16.dp,
							bottom = 8.dp,
							start = 16.dp,
							end = 16.dp
						)
				)
			}

			items(
				items = plans.plans,
				key = { it.id }
			) { planData ->
				val planProgress by vm.plansProgress.filter { it.planId == planData.id }
					.collectAsState(initial = PlanProgress(planData.id, 0f))
				val planWithProgress = planData.copy(progress = planProgress.progress)
				PlanCard(
					planData = planWithProgress,
					onClick = {
						vm.setCurrentPlan(planData.id)
						navController.popBackStack()
					}
				) {
					var showDropdownMenu by remember {
						mutableStateOf(false)
					}
					IconButton(onClick = { showDropdownMenu = true }) {
						Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
					}
					NormalPlanDropdownMenu(
						expanded = showDropdownMenu,
						onDismissRequest = { showDropdownMenu = false },
						onRename = {
							showRenameDialog = planData
						},
						onDelete = {
							vm.deletePlans(listOf(planData))
						}
					)
				}
			}
		}
	}

	if (showRenameDialog != null) {
		val renamedPlan = showRenameDialog!!
		RenameDialog(
			onCloseDialog = { showRenameDialog = null },
			name = renamedPlan.title,
			label = { Text(text = stringResource(id = R.string.new_title)) },
			onRename = { newTitle ->
				vm.renamePlanTitle(renamedPlan.id, newTitle)
			}
		)
	}

	if (showCreateDialog) {
		CreatePlanDialog(
			onCloseDialog = {
				showCreateDialog = false
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
		optionMenuItems = { closeMenu ->
			DropdownMenuItem(
				text = { Text(text = "Test") },
				onClick = {
					closeMenu()
				}
			)
		},
		vm = PlanListViewModel
	)
}