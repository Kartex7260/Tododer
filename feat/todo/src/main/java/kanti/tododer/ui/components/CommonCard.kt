package kanti.tododer.ui.components

import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kanti.tododer.ui.common.Selectable
import kanti.tododer.ui.common.Visible
import kanti.tododer.ui.components.selection.SelectionBox

@Composable
fun <T> CommonCard(
	modifier: Modifier = Modifier,
	selection: Boolean = false,
	state: T,
	onChangeSelect: (Boolean) -> Unit = {},
	checkboxColors: CheckboxColors = CheckboxDefaults.colors(),
	card: @Composable () -> Unit
) where T : Selectable, T : Visible {
	DeleteAnimationVisible(
		modifier = modifier,
		visible = state.visible
	) {
		SelectionBox(
			selection = selection,
			selected = state.selected,
			onChangeSelected = onChangeSelect,
			checkboxColors = checkboxColors,
			content = card
		)
	}
//	AnimatedContent(
//		modifier = Modifier.fillMaxSize(),
//		targetState = state.visible,
//		transitionSpec = {
//			ContentTransform(
//				targetContentEnter = expandVertically() + scaleIn() + fadeIn(),
//				initialContentExit = shrinkVertically() + scaleOut() + fadeOut()
//			)
//		},
//		contentAlignment = Alignment.TopCenter,
//		label = "animatedCard"
//	) { visible ->
//		if (visible) {
//			SelectionBox(
//				modifier = modifier,
//				selection = selection,
//				selected = state.selected,
//				onChangeSelected = onChangeSelect,
//				checkboxColors = checkboxColors,
//				content = card
//			)
//		}
//	}
}