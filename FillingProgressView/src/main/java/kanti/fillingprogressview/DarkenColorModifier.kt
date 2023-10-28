package kanti.fillingprogressview

import android.graphics.Color

class DarkenColorModifier(
	colorModifier: ColorModifier? = null
) : ColorModifier(colorModifier) {

	override fun onModify(color: Int, modifier: Float): Int {
		require(modifier in Progress.MIN_VALUE..Progress.MAX_VALUE) {
			"The modification cannot be more than ${Progress.MAX_VALUE} " +
					"or less than ${Progress.MIN_VALUE}. Actual: $modifier"
		}

		val red = Color.red(color)
		val green = Color.green(color)
		val blue = Color.blue(color)
		val alpha = Color.alpha(color)
		return Color.argb(
			alpha,
			modifyColor(red, modifier),
			modifyColor(green, modifier),
			modifyColor(blue, modifier)
		)
	}

	private fun modifyColor(color: Int, modifier: Float): Int {
		return (modifier * color).toInt()
	}

}