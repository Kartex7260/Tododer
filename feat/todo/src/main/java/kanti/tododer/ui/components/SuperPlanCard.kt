package kanti.tododer.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kanti.tododer.ui.common.PlanUiState
import kanti.tododer.ui.components.menu.NormalPlanDropdownMenu
import kanti.tododer.ui.components.plan.PlanCard
import kanti.tododer.ui.components.plan.PlanData

@Composable
fun SuperPlanCard(
	modifier: Modifier = Modifier,
	selection: Boolean = false,
	planUiState: PlanUiState,
	onLongClick: (PlanData) -> Unit = {},
	onClick: (PlanData) -> Unit = {},
	onChangeSelect: (PlanData, Boolean) -> Unit = { _, _ -> },
	menuOnRename: (PlanData) -> Unit = {},
	menuOnDelete: (PlanData) -> Unit = {}
) = CommonCard(
	modifier = modifier,
	selection = selection,
	state = planUiState,
	onChangeSelect = { onChangeSelect(planUiState.data, it) }
) {
	val planData = planUiState.data
	PlanCard(
		planData = planData,
		onLongClick = { onLongClick(planData) },
		onClick = {
			if (selection)
				onChangeSelect(planData, !planUiState.selected)
			else
				onClick(planData)
		}
	) {
		AnimatedVisibility(
			visible = !selection,
			enter = fadeIn(),
			exit = fadeOut()
		) {
			var showDropdownMenu by remember {
				mutableStateOf(false)
			}
			IconButton(onClick = { showDropdownMenu = true }) {
				Icon(
					imageVector = Icons.Default.MoreVert,
					contentDescription = null
				)
			}
			NormalPlanDropdownMenu(
				expanded = showDropdownMenu,
				onDismissRequest = { showDropdownMenu = false },
				onRename = { menuOnRename(planData) },
				onDelete = { menuOnDelete(planData) }
			)
		}
	}
}