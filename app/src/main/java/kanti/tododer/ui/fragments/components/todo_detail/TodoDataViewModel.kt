package kanti.tododer.ui.fragments.components.todo_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kanti.tododer.ui.state.TodoElement

class TodoDataViewModel : ViewModel() {

	private val _todoElementLiveData = MutableLiveData<TodoDataUiState>()
	val todoElementLiveData: LiveData<TodoDataUiState> = _todoElementLiveData

	fun setTodoElement(todoElement: TodoElement? = null) {
		_todoElementLiveData.value = TodoDataUiState(
			todoElement
		)
	}

}