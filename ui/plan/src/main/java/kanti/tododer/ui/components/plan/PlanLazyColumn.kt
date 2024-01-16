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
	contentPadding: PaddingValues = PaddingValues(
		top = 12.dp,
		bottom = 12.dp,
		start = 16.dp,
		end = 16.dp
	),
	flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
	userScrollEnabled: Boolean = true,
	preContent: @Composable () -> Unit = {},
	content: PlansData = PlansData(),
	postContent: @Composable () -> Unit = {},
	onClick: (plan: PlanData) -> Unit,
	action: (@Composable (plan: PlanData) -> Unit)? = null
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
				.padding(bottom = 8.dp),
			planData = uiState,
			onClick = { onClick(uiState) }
		) {
			if (action != null) {
				action(uiState)
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
		content = PlansData(listOf(
			PlanData(0, "Hi", 0.3f),
			PlanData(1, "Work", 1f),
			PlanData(2, "Foo", 0.8f)
		)),
		action = {
			IconButton(onClick = {  }) {
				Icon(
					imageVector = Icons.Default.MoreVert,
					contentDescription = null
				)
			}
		}
	)
}