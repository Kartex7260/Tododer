package kanti.fillingprogressview

abstract class ColorModifier(
	var colorModifier: ColorModifier? = null
) {

	fun modify(color: Int, modifier: Float): Int {
		val modifiedColor = onModify(color, modifier)
		return colorModifier?.modify(modifiedColor, modifier) ?: modifiedColor
	}

	protected abstract fun onModify(color: Int, modifier: Float): Int

	companion object {

		const val MAX_COLOR_VALUE = 255

	}

}