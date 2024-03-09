package kanti.tododer.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import kanti.tododer.feat.todo.R

@Composable
fun TodoFloatingActionButton(
	selection: Boolean = false,
	allowGrouping: Boolean = true,
	onClick: () -> Unit = {},
	onGroup: () -> Unit = {},
	onCheck: () -> Unit = {},
	onDelete: () -> Unit = {}
) = MultiFloatingActionButton(
	altFab = selection,
	initExpandSmallFabState = true,
	onClick = onClick,
	smallFabContent = { spacerBetweenSmallFab ->
		val containerColor = MaterialTheme.colorScheme.secondaryContainer
		if (allowGrouping) {
			SmallFloatingActionButton(
				onClick = onGroup,
				containerColor = containerColor
			) {
				Icon(painter = painterResource(id = R.drawable.segment), contentDescription = null)
			}
			spacerBetweenSmallFab()
		}
		SmallFloatingActionButton(
			onClick = onCheck,
			containerColor = containerColor
		) {
			Icon(painter = painterResource(id = R.drawable.check_box), contentDescription = null)
		}
		spacerBetweenSmallFab()
		SmallFloatingActionButton(
			onClick = onDelete,
			containerColor = containerColor
		) {
			Icon(imageVector = Icons.Default.Delete, contentDescription = null)
		}
	},
	containerColor = MaterialTheme.colorScheme.tertiaryContainer,
	altFabContent = {
		Icon(painter = painterResource(id = R.drawable.multi_select), contentDescription = null)
	},
	fabContent = {
		Icon(imageVector = Icons.Default.Add, contentDescription = null)
	}
)

@Composable
fun PlanFloatingActionButton(
	selection: Boolean = false,
	onClick: () -> Unit = {},
	onDelete: () -> Unit = {}
) = MultiFloatingActionButton(
	altFab = selection,
	initExpandSmallFabState = true,
	onClick = onClick,
	smallFabContent = { spacerBetweenSmallFab ->
		spacerBetweenSmallFab()
		SmallFloatingActionButton(
			onClick = onDelete,
			containerColor = MaterialTheme.colorScheme.secondaryContainer
		) {
			Icon(imageVector = Icons.Default.Delete, contentDescription = null)
		}
	},
	containerColor = MaterialTheme.colorScheme.tertiaryContainer,
	altFabContent = {
		Icon(painter = painterResource(id = R.drawable.multi_select), contentDescription = null)
	},
	fabContent = {
		Icon(imageVector = Icons.Default.Add, contentDescription = null)
	}
)