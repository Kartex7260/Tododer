package kanti.tododer.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kanti.fillingprogressbar.FPBDefaults
import kanti.tododer.ui.common.MultiSelectionStyle
import kanti.tododer.ui.common.PlanUiState
import kanti.tododer.ui.common.anim.allowSelectionColor
import kanti.tododer.ui.components.menu.NormalPlanDropdownMenu
import kanti.tododer.ui.components.plan.PlanCard
import kanti.tododer.ui.components.plan.PlanData

@Composable
fun SuperPlanCard(
	modifier: Modifier = Modifier,
	selectionStyle: MultiSelectionStyle = MultiSelectionStyle.ColorFill,
	selection: Boolean = false,
	planUiState: PlanUiState,
	onLongClick: (PlanData) -> Unit = {},
	onClick: (PlanData) -> Unit = {},
	onChangeSelect: (PlanData, Boolean) -> Unit = { _, _ -> },
	menuOnRename: (PlanData) -> Unit = {},
	menuOnDelete: (PlanData) -> Unit = {}
) {
	val allowColorFill = selectionStyle.contains(MultiSelectionStyle.ColorFill)
	val allowCheckbox = selectionStyle.contains(MultiSelectionStyle.Checkbox)
	val selected = selection && planUiState.selected

	CommonCard(
		modifier = modifier,
		selection = selection && allowCheckbox,
		state = planUiState,
		onChangeSelect = { onChangeSelect(planUiState.data, it) },
		checkboxColors = CheckboxDefaults.colors(
			checkedColor = allowSelectionColor(
				allow = allowColorFill,
				selected = selected,
				selectedColor = MaterialTheme.colorScheme.tertiary,
				normalColor = MaterialTheme.colorScheme.primary
			),
			checkmarkColor = MaterialTheme.colorScheme.surface
		)
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
			},
			cardColors = CardDefaults.cardColors(
				containerColor = allowSelectionColor(
					allow = allowColorFill,
					selected = selected,
					selectedColor = MaterialTheme.colorScheme.tertiaryContainer,
					normalColor = MaterialTheme.colorScheme.surfaceVariant
				)
			),
			fpbColors = FPBDefaults.fpbColors(
				backStrokeColor = allowSelectionColor(
					allow = allowColorFill,
					selected = selected,
					selectedColor = MaterialTheme.colorScheme.onTertiaryContainer,
					normalColor = MaterialTheme.colorScheme.onSurfaceVariant
				),
				frontColor = allowSelectionColor(
					allow = allowColorFill,
					selected = selected,
					selectedColor = MaterialTheme.colorScheme.tertiary,
					normalColor = MaterialTheme.colorScheme.primary
				)
			)
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
}