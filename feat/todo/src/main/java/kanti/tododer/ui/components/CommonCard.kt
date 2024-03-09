package kanti.tododer.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
//	DeleteAnimationVisible(
//		modifier = modifier,
//		visible = state.visible
//	) {
//		SelectionBox(
//			modifier = modifier,
//			selection = selection,
//			selected = state.selected,
//			onChangeSelected = onChangeSelect,
//			checkboxColors = checkboxColors,
//			content = card
//		)
//	}
	AnimatedContent(
		modifier = Modifier.fillMaxSize(),
		targetState = state.visible,
		transitionSpec = {
			ContentTransform(
				targetContentEnter = expandVertically() + scaleIn() + fadeIn(),
				initialContentExit = shrinkVertically() + scaleOut() + fadeOut()
			)
		},
		contentAlignment = Alignment.TopCenter,
		label = "animatedCard"
	) { visible ->
		if (visible) {
			SelectionBox(
				modifier = modifier,
				selection = selection,
				selected = state.selected,
				onChangeSelected = onChangeSelect,
				checkboxColors = checkboxColors,
				content = card
			)
		}
	}

}