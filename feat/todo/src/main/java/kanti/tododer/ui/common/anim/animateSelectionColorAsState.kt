package kanti.tododer.ui.common.anim

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color

@Composable
fun animateSelectionColorAsState(
	selected: Boolean = false,
	selectedColor: Color,
	normalColor: Color,
	label: String = "animateColor"
): State<Color> {
	return animateColorAsState(
		targetValue = if (selected)
			selectedColor
		else
			normalColor,
		label = label
	)
}

@Composable
fun allowSelectionColor(
	allow: Boolean = false,
	selected: Boolean = false,
	selectedColor: Color,
	normalColor: Color
): Color {
	return if (allow)
		animateSelectionColorAsState(
			selected = selected,
			selectedColor = selectedColor,
			normalColor = normalColor
		).value
	else normalColor
}