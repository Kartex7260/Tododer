package kanti.fillingprogressbar

import androidx.annotation.FloatRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color

@Immutable
class FillingProgressBarColors internal constructor(
	private val backStrokeColor: Color,
	private val frontColor: Color,
	private val disabledColor: Color,
	private val bodyStrokeLightRatio: Float
) {

	@Composable
	internal fun backStrokeColor(enabled: Boolean): State<Color> {
		return rememberUpdatedState(
			newValue = if (enabled) backStrokeColor else disabledColor
		)
	}

	@Composable
	internal fun frontStrokeColor(
		enabled: Boolean,
		@FloatRange(from = 0.0, to = 1.0) progress: Float
	): State<Color> {
		checkProgress(progress)
		return rememberUpdatedState(
			newValue = if (enabled) {
				frontColor.copy(
					alpha = frontColor.alpha * progress,
					red = frontColor.red * bodyStrokeLightRatio,
					green = frontColor.green * bodyStrokeLightRatio,
					blue = frontColor.blue * bodyStrokeLightRatio
				)
			} else {
				disabledColor.copy(alpha = disabledColor.alpha * progress)
			}
		)
	}

	@Composable
	internal fun bodyColor(
		enabled: Boolean,
		@FloatRange(from = 0.0, to = 1.0) progress: Float
	): State<Color> {
		checkProgress(progress)
		return rememberUpdatedState(
			newValue = if (enabled) {
				frontColor.copy(
					alpha = frontColor.alpha * progress
				)
			} else {
				disabledColor.copy(
					alpha = disabledColor.alpha * progress
				)
			}
		)
	}

	private fun checkProgress(progress: Float) {
		if (progress !in 0.0f..1.0f) {
			throw IllegalArgumentException("The progress cannot be less than 0 or more than 1")
		}
	}
}