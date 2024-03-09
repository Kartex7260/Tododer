package kanti.tododer.ui.components.multiselection

import kanti.tododer.ui.common.MultiSelectionStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class Selector(
	init: Set<MultiSelectionStyle> = setOf()
) {

	private val _selected = MutableStateFlow(init)
	val selected: StateFlow<Set<MultiSelectionStyle>> = _selected.asStateFlow()

	fun setSelect(mss: MultiSelectionStyle, selected: Boolean) {
		if (selected) {
			_selected.update {
				it.plus(mss)
			}
		} else {
			_selected.update {
				it.minus(mss)
			}
		}
	}
}