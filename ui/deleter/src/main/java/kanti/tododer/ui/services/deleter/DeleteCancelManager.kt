package kanti.tododer.ui.services.deleter

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

@Stable
class DeleteCancelManager<Value>(
	val toKey: Value.() -> Long,
	private val onDelete: suspend (List<Value>) -> Unit = {}
) {

	private val _deleted = MutableStateFlow<Map<Long, Value>>(mapOf())
	val deletedValues: StateFlow<Map<Long, Value>> = _deleted.asStateFlow()

	private val _onDeleted = MutableSharedFlow<List<Value>>()
	val onDeleted = _onDeleted.asSharedFlow()

	suspend fun delete(values: List<Value>) {
		rejectCancelChance()

		val deletedMap = HashMap<Long, Value>()
		for (value in values) {
			deletedMap[value.toKey()] = value
		}
		_deleted.emit(deletedMap)

		_onDeleted.emit(_deleted.value.values.toList())
	}

	suspend fun cancelDelete() {
		_deleted.emit(HashMap())
	}

	suspend fun rejectCancelChance() {
		if (_deleted.value.isEmpty())
			return
		onDelete(_deleted.value.values.toList())
		_deleted.emit(HashMap())
	}
}