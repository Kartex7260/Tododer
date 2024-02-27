package kanti.tododer.ui.components.selection

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SelectionController @Inject constructor() {

    private val selected = mutableSetOf<Long>()

    private val _selectionState = MutableStateFlow(SelectionUiState())
    val selectionState: Flow<SelectionUiState> = _selectionState.asStateFlow()

    var selection: Boolean
        get() = _selectionState.value.selection
        set(value) {
            _selectionState.update {
                it.copy(
                    selection = value
                )
            }
        }

    fun setSelect(id: Long, select: Boolean) {
        if (select) {
            selected.add(id)
        } else {
            selected.remove(id)
        }
        updateState()
    }

    fun setSelect(pairs: List<Pair<Long, Boolean>>) {
        for (pair in pairs) {
            if (pair.second) {
                selected.add(pair.first)
            } else {
                selected.remove(pair.first)
            }
        }
        updateState()
    }

    private fun updateState() = _selectionState.update { it.copy(selected = selected) }
}