package kanti.tododer.ui.fragments.components.todo_data

import androidx.lifecycle.ViewModel
import kanti.tododer.data.model.common.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TodoDataViewModel : ViewModel() {

	private val _todoElement = MutableStateFlow<TodoDataUiState>(TodoDataUiState())
	val todoElement = _todoElement.asStateFlow()

	fun sendTodo(todoElement: Todo? = null) {
		_todoElement.value = TodoDataUiState(
			todoElement
		)
	}

}