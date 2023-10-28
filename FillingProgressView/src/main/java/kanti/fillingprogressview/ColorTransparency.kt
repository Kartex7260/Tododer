package kanti.fillingprogressview

import android.graphics.Color

class ColorTransparency(
	private val maxColor: Int,
	private val modifierMaxColor: Float = Progress.MAX_VALUE
) {

	fun transparent(modifier: Float): Int {
		require(modifier in Progress.MIN_VALUE..Progress.MAX_VALUE) {
			"The modification cannot be more than ${Progress.MAX_VALUE} " +
					"or less than ${Progress.MIN_VALUE}. Actual: $modifier"
		}

		val red = Color.red(maxColor)
		val green = Color.green(maxColor)
		val blue = Color.blue(maxColor)
		val modifierInt = modifierToInt(modifier)
		return Color.argb(modifierInt, red, green, blue)
	}

	private fun modifierToInt(modifier: Float): Int {
		return (modifier * MAX_COLOR_VALUE * modifierMaxColor).toInt()
	}

	companion object {

		const val MAX_COLOR_VALUE = 255

	}

}