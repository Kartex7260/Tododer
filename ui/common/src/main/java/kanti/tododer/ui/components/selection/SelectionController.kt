package kanti.tododer.ui.components.selection

import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SelectionController @Inject constructor(
    @StandardLog private val logger: Logger
) {

    private val mSelected = mutableSetOf<Long>()

    private val _selectionState = MutableStateFlow(SelectionUiState())
    val selectionState: StateFlow<SelectionUiState> = _selectionState.asStateFlow()

    var selection: Boolean
        get() = _selectionState.value.selection
        set(value) {
            logger.d(LOG_TAG, "setSelection(Boolean = $value)")
            if (_selectionState.value.selection == value)
                return
            _selectionState.update {
                it.copy(
                    selection = value
                )
            }
        }

    val selected: List<Long> get() = mSelected.toList()

    fun switchSelection() {
        if (selection) {
            clear()
        } else {
            selection = true
        }
    }

    fun setSelect(id: Long, select: Boolean) {
        logger.d(LOG_TAG, "setSelect(Long = $id, Boolean = $select)")
        if (select) {
            mSelected.add(id)
        } else {
            mSelected.remove(id)
        }
        updateState()
    }

    fun clear() {
        mSelected.clear()
        _selectionState.update {
            it.copy(selection = false, selected = mSelected.toSet())
        }
    }

    private fun updateState() {
        _selectionState.update { it.copy(selected = mSelected.toSet()) }
    }

    companion object {

        private const val LOG_TAG = "SelectionController"
    }
}