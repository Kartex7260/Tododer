package kanti.tododer.ui.fragments.components

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kanti.tododer.ui.state.TodoElement

class TodoListViewModel : ViewModel() {

	private val _todoListLiveData = MutableLiveData<List<TodoElement>>()
	val todoListLiveData: LiveData<List<TodoElement>> = _todoListLiveData

	fun setTodoList(list: List<TodoElement>) {
		_todoListLiveData.value = list
	}

}