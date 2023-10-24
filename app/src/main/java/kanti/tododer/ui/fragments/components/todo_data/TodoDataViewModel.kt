package kanti.tododer.ui.fragments.components.todo_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kanti.tododer.data.model.common.Todo

class TodoDataViewModel : ViewModel() {

	private val _todoElementLiveData = MutableLiveData<TodoDataUiState>()
	val todoElementLiveData: LiveData<TodoDataUiState> = _todoElementLiveData

	fun sendTodo(todoElement: Todo? = null) {
		_todoElementLiveData.value = TodoDataUiState(
			todoElement
		)
	}

}