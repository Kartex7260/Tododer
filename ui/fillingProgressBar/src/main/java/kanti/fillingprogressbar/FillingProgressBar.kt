package kanti.fillingprogressbar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp

@Composable
fun FillingProgressBar(
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	progress: Float = FPBDefaults.Progress,
	diameter: Dp = FPBDefaults.Diameter,
	strokeWidth: Dp = FPBDefaults.StrokeWidth,
	contentPadding: PaddingValues = FPBDefaults.ContentPadding,
	colors: FillingProgressBarColors = FPBDefaults.fpbColors()
) {
	val backStrokeColor by colors.backStrokeColor(enabled = enabled)
	val frontStrokeColor by colors.frontStrokeColor(enabled = enabled, progress = progress)
	val bodyColor by colors.bodyColor(enabled = enabled, progress = progress)
	Canvas(
		modifier = Modifier
			.defaultMinSize(
				minWidth = diameter,
				minHeight = diameter
			)
			.padding(paddingValues = contentPadding)
			.then(modifier)
	) {
		val radius = (diameter / 2).toPx()
		val strokeRadius = ((diameter - strokeWidth) / 2).toPx()
		val stroke = Stroke(
			width = strokeWidth.toPx()
		)

		drawCircle(
			color = backStrokeColor,
			radius = strokeRadius,
			style = stroke
		)
		drawCircle(
			color = bodyColor,
			radius = radius
		)
		drawCircle(
			color = frontStrokeColor,
			radius = strokeRadius,
			style = stroke
		)
	}
}

@Preview(
	name = "100",
	showBackground = true,
	group = "enabled"
)
@Composable
private fun PreviewFillingProgressBarEnabled100() {
	FillingProgressBar(
		progress = 1f
	)
}

@Preview(
	name = "75",
	showBackground = true,
	group = "enabled"
)
@Composable
private fun PreviewFillingProgressBarEnabled75() {
	FillingProgressBar(
		progress = 0.75f
	)
}

@Preview(
	name = "50",
	showBackground = true,
	group = "enabled"
)
@Composable
private fun PreviewFillingProgressBarEnabled50() {
	FillingProgressBar(
		progress = 0.5f
	)
}

@Preview(
	name = "25",
	showBackground = true,
	group = "enabled"
)
@Composable
private fun PreviewFillingProgressBarEnabled25() {
	FillingProgressBar(
		progress = 0.25f
	)
}

@Preview(
	name = "0",
	showBackground = true,
	group = "enabled"
)
@Composable
private fun PreviewFillingProgressBarEnabled0() {
	FillingProgressBar(
		progress = 0f
	)
}

@Preview(
	name = "100",
	showBackground = true, group = "disabled"
)
@Composable
private fun PreviewFillingProgressBarDisabled100() {
	FillingProgressBar(
		progress = 1f,
		enabled = false
	)
}

@Preview(
	name = "75",
	showBackground = true,
	group = "disabled"
)
@Composable
private fun PreviewFillingProgressBarDisabled75() {
	FillingProgressBar(
		progress = 0.75f,
		enabled = false
	)
}

@Preview(
	name = "50",
	showBackground = true,
	group = "disabled"
)
@Composable
private fun PreviewFillingProgressBarDisabled50() {
	FillingProgressBar(
		progress = 0.5f,
		enabled = false
	)
}

@Preview(
	name = "25",
	showBackground = true,
	group = "disabled"
)
@Composable
private fun PreviewFillingProgressBarDisabled25() {
	FillingProgressBar(
		progress = 0.25f,
		enabled = false
	)
}

@Preview(
	name = "0",
	showBackground = true,
	group = "disabled"
)
@Composable
private fun PreviewFillingProgressBarDisabled0() {
	FillingProgressBar(
		progress = 0f,
		enabled = false
	)
}