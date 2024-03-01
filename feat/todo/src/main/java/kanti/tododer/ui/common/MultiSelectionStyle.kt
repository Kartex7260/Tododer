package kanti.tododer.ui.common

class MultiSelectionStyle(
	private val flag: Int
) {

	fun contains(style: MultiSelectionStyle): Boolean {
		val clearFlag = flag and style.flag
		return clearFlag == style.flag
	}

	operator fun plus(style: MultiSelectionStyle): MultiSelectionStyle {
		return MultiSelectionStyle(flag or style.flag)
	}

	companion object {

		val ColorFill = MultiSelectionStyle(0x00000001)
		val Checkbox = MultiSelectionStyle(0x00000010)
	}
}