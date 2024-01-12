package kanti.tododer.ui.services.deleter

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

@Stable
class DeleteCancelManager<Value>(
	val toKey: Value.() -> Int,
	private val onDelete: suspend (List<Value>) -> Unit = {}
) {

	private val _deleted = MutableStateFlow<Map<Int, Value>>(mapOf())
	val deletedValues: StateFlow<Map<Int, Value>> = _deleted.asStateFlow()

	private val _onDeleted = MutableSharedFlow<List<Value>>()
	val onDeleted = _onDeleted.asSharedFlow()

	suspend fun delete(values: List<Value>) {
		cancelChanceReject()

		val deletedMap = HashMap<Int, Value>()
		for (value in values) {
			deletedMap[value.toKey()] = value
		}
		_deleted.emit(deletedMap)

		_onDeleted.emit(_deleted.value.values.toList())
	}

	suspend fun cancelDelete() {
		_deleted.emit(HashMap())
	}

	suspend fun cancelChanceReject() {
		if (_deleted.value.isEmpty())
			return
		onDelete(_deleted.value.values.toList())
		_deleted.emit(HashMap())
	}
}