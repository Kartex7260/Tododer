package kanti.tododer.ui.components.plan

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PlanLazyColumn(
	modifier: Modifier = Modifier,
	state: LazyListState = rememberLazyListState(),
	contentPadding: PaddingValues = PaddingValues(all = 16.dp),
	flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
	userScrollEnabled: Boolean = true,
	preContent: @Composable () -> Unit = {},
	content: PlansUiState = PlansUiState(),
	postContent: @Composable () -> Unit = {},
	onClick: (plan: PlanUiState) -> Unit,
	endButton: (@Composable (plan: PlanUiState) -> Unit)? = null
) = LazyColumn(
	modifier = modifier,
	state = state,
	contentPadding = contentPadding,
	flingBehavior = flingBehavior,
	userScrollEnabled = userScrollEnabled
) {
	item {
		preContent()
	}
	items(
		items = content.plans,
		key = { it.id }
	) { uiState ->
		PlanCard(
			modifier = Modifier
				.padding(bottom = 16.dp),
			planUiState = uiState,
			onClick = { onClick(uiState) }
		) {
			if (endButton != null) {
				endButton(uiState)
			}
		}
	}
	item {
		postContent()
	}
}

@Preview(
	showBackground = true
)
@Composable
private fun PreviewPlanLazyColumn() {
	PlanLazyColumn(
		modifier = Modifier.fillMaxSize(),
		onClick = {  },
		content = PlansUiState(listOf(
			PlanUiState(0, "Hi", 0.3f),
			PlanUiState(1, "Work", 1f),
			PlanUiState(2, "Foo", 0.8f)
		)),
		endButton = {
			IconButton(onClick = {  }) {
				Icon(
					imageVector = Icons.Default.MoreVert,
					contentDescription = null
				)
			}
		}
	)
}