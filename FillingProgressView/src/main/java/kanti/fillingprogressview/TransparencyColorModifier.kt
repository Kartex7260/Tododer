package kanti.fillingprogressview

import android.graphics.Color

class TransparencyColorModifier(
	colorModifier: ColorModifier? = null,
	private val modifierMaxColor: Float = Progress.MAX_VALUE
) : ColorModifier(colorModifier) {

	override fun onModify(color: Int, modifier: Float): Int {
		require(modifier in Progress.MIN_VALUE..Progress.MAX_VALUE) {
			"The modification cannot be more than ${Progress.MAX_VALUE} " +
					"or less than ${Progress.MIN_VALUE}. Actual: $modifier"
		}

		val red = Color.red(color)
		val green = Color.green(color)
		val blue = Color.blue(color)
		val modifierInt = modifyColor(modifier)
		return Color.argb(modifierInt, red, green, blue)
	}

	private fun modifyColor(modifier: Float): Int {
		return (modifier * MAX_COLOR_VALUE * modifierMaxColor).toInt()
	}

}