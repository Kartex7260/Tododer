package kanti.tododer.ui.components

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
			content = card
		)
	}
}