package kanti.tododer.ui.screen.plan_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.components.plan.PlanCard
import kanti.tododer.ui.screen.plan_list.viewmodel.PlanListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlanListTopBar(
	navController: NavController,
	topBarActions: (@Composable () -> Unit)?
) {
	CenterAlignedTopAppBar(
		title = {
			Text(text = stringResource(id = R.string.plans))
		},
		navigationIcon = {
			IconButton(onClick = { navController.popBackStack() }) {
				Icon(
					imageVector = Icons.Default.ArrowBack,
					contentDescription = null
				)
			}
		},
		actions = {
			if (topBarActions != null) {
				topBarActions()
			}
		}
	)
}

@Composable
fun PlanListScreen(
	navController: NavController = rememberNavController(),
	topBarActions: (@Composable () -> Unit)? = null,
	vm: PlanListViewModel = PlanListViewModel
) {
	val planAll by vm.planAll.collectAsState()
	val planDefault by vm.planDefault.collectAsState()
	val plans by vm.plans.collectAsState()
	var showDialog by rememberSaveable {
		mutableStateOf(false)
	}

	LaunchedEffect(key1 = vm) {
		vm.newPlanCreated.collect {
			navController.popBackStack()
		}
	}

	Scaffold(
		topBar = {
			PlanListTopBar(
				navController = navController,
				topBarActions = topBarActions
			)
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
		Column(
			modifier = Modifier
				.padding(paddingValues)
				.verticalScroll(rememberScrollState())
		) {
			PlanCard(
				modifier = Modifier
					.padding(
						start = 16.dp,
						end = 16.dp,
						top = 16.dp
					),
				planUiState = planAll,
				onClick = {
					vm.setCurrentPlan(planAll.id)
					navController.popBackStack()
				}
			) {}

			PlanCard(
				modifier = Modifier
					.padding(
						start = 16.dp,
						end = 16.dp,
						top = 16.dp
					),
				planUiState = planDefault,
				onClick = {
					vm.setCurrentPlan(planDefault.id)
					navController.popBackStack()
				}
			) {}

			Divider(
				modifier = Modifier
					.padding(
						top = 16.dp,
						start = 32.dp,
						end = 32.dp
					)
			)

			for (plan in plans.plans) {
				key(plan.id) {
					PlanCard(
						modifier = Modifier.
							padding(
								start = 16.dp,
								end = 16.dp,
								top = 16.dp
							),
						planUiState = plan,
						onClick = {
							vm.setCurrentPlan(plan.id)
							navController.popBackStack()
						}
					) {}
				}
			}
		}
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