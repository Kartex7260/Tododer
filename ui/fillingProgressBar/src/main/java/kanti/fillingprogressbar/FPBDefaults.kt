package kanti.fillingprogressbar

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object FPBDefaults {

	const val Progress: Float = 0.0f
	val Diameter: Dp = 20.dp
	val StrokeWidth: Dp = 2.dp

	private val Padding: Dp = 24.dp
	val ContentPadding = PaddingValues(all = Padding)
	val NoContentPadding = PaddingValues(all = 0.dp)

	private const val BodyStrokeLightRatio: Float = 0.9f

	@Composable
	fun fpbColors(
		backStrokeColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
		frontColor: Color = MaterialTheme.colorScheme.primary,
		disabledColor: Color = MaterialTheme.colorScheme.onSurface.copy(
			alpha = 0.38f
		),
		@FloatRange(from = 0.0, to = 1.0) bodyStrokeLightRatio: Float = BodyStrokeLightRatio
	) = FillingProgressBarColors(
		backStrokeColor = backStrokeColor,
		frontColor = frontColor,
		disabledColor = disabledColor,
		bodyStrokeLightRatio = bodyStrokeLightRatio
	)

	@Composable
	fun secondaryFpbColors(
		backStrokeColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
		frontColor: Color = MaterialTheme.colorScheme.secondary,
		disabledColor: Color = MaterialTheme.colorScheme.onSurface.copy(
			alpha = 0.38f
		),
		@FloatRange(from = 0.0, to = 1.0) bodyStrokeLightRatio: Float = BodyStrokeLightRatio
	) = fpbColors(
		backStrokeColor = backStrokeColor,
		frontColor = frontColor,
		disabledColor = disabledColor,
		bodyStrokeLightRatio = bodyStrokeLightRatio
	)

	@Composable
	fun tertiaryFpbColors(
		backStrokeColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
		frontColor: Color = MaterialTheme.colorScheme.tertiary,
		disabledColor: Color = MaterialTheme.colorScheme.onSurface.copy(
			alpha = 0.38f
		),
		@FloatRange(from = 0.0, to = 1.0) bodyStrokeLightRatio: Float = BodyStrokeLightRatio
	) = fpbColors(
		backStrokeColor = backStrokeColor,
		frontColor = frontColor,
		disabledColor = disabledColor,
		bodyStrokeLightRatio = bodyStrokeLightRatio
	)
}