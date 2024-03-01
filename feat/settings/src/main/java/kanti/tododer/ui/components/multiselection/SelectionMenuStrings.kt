package kanti.tododer.ui.components.multiselection

import androidx.compose.runtime.Immutable
import kanti.tododer.ui.common.MultiSelectionStyle

@Immutable
data class SelectionMenuStrings(
	val selectionStyle: String,
	val colorFill: String,
	val checkbox: String,
	val setSelectionStyle: String,
	val close: String
) {

	fun fromMultiSelectionStyle(selectionStyle: MultiSelectionStyle): String {
		return when (selectionStyle) {
			MultiSelectionStyle.ColorFill -> colorFill
			MultiSelectionStyle.Checkbox -> checkbox
		}
	}
}
