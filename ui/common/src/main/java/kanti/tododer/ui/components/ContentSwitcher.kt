package kanti.tododer.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun ContentSwitcher(
	state: Boolean = false,
	trueContent: @Composable AnimatedVisibilityScope.() -> Unit,
	falseContent: @Composable AnimatedVisibilityScope.() -> Unit,
	enter: EnterTransition = fadeIn(),
	exit: ExitTransition = fadeOut()
) {
	Box(
		contentAlignment = Alignment.Center
	) {
		AnimatedVisibility(
			visible = state,
			enter = enter,
			exit = exit,
			content = trueContent
		)
		AnimatedVisibility(
			visible = !state,
			enter = enter,
			exit = exit,
			content = falseContent
		)
	}
}