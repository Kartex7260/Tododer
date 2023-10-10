package kanti.tododer.ui.fragments.components.todo_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kanti.tododer.ui.state.TodoElement

class TodoListViewModel : ViewModel() {

	private val _todoListLiveData = MutableLiveData<TodoListUiState>()
	val todoListLiveData: LiveData<TodoListUiState> = _todoListLiveData

	fun setTodoList(list: List<TodoElement> = listOf(), onElementClick: OnTodoElementClick) {
		_todoListLiveData.value = TodoListUiState(list, onElementClick)
	}

}