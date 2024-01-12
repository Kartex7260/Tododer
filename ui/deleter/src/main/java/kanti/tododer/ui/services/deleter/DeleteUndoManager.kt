package kanti.tododer.ui.services.deleter

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Stable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Stable
class DeleteUndoManager<Value>(
	private val snackbarHostState: SnackbarHostState,
	private val coroutineScope: CoroutineScope,
	val toKey: Value.() -> Int,
	private val toSnackbarMessage: List<Value>.() -> String,
	private val cancelLabel: String,
	private val onDelete: (List<Value>) -> Unit = {}
) {

	private var job: Job? = null

	private val mutex = Mutex()

	private val _deleted = MutableStateFlow<Map<Int, Value>>(mapOf())
	val deleted: StateFlow<Map<Int, Value>> = _deleted.asStateFlow()

	fun delete(values: List<Value>) {
		job?.cancel()
		job = coroutineScope.launch {
			cancelChanceReject()

			mutex.withLock {
				val deletedMap = HashMap<Int, Value>()
				for (value in values) {
					deletedMap[value.toKey()] = value
				}
				_deleted.emit(deletedMap)
			}

			val result = snackbarHostState.showSnackbar(
				message = values.toSnackbarMessage(),
				actionLabel = cancelLabel,
				withDismissAction = true,
				duration = SnackbarDuration.Short
			)
			when (result) {
				SnackbarResult.ActionPerformed -> {
					mutex.withLock {
						_deleted.value = HashMap()
					}
				}
				SnackbarResult.Dismissed -> {
					cancelChanceReject()
				}
			}
		}
	}

	suspend fun cancelChanceReject() {
		mutex.withLock {
			if (_deleted.value.isEmpty())
				return@withLock
			onDelete(_deleted.value.values.toList())
			_deleted.value = HashMap()
		}
	}
}