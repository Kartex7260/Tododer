package kanti.tododer.ui.fragments.components.todo_data.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kanti.tododer.data.model.common.Todo

class TodoDataSaveLifecycleObserver(
	lifecycleOwner: LifecycleOwner,
	private val todoSavable: TodoSavable,
) : DefaultLifecycleObserver {

	var todo: kanti.tododer.data.model.common.Todo? = null

	init {
		lifecycleOwner.lifecycle.addObserver(this)
	}

	override fun onStop(owner: LifecycleOwner) {
		super.onStop(owner)
		if (todo == null)
			return

		todoSavable.onSave(todo!!, TodoSavable.Type.TITLE)
		todoSavable.onSave(todo!!, TodoSavable.Type.REMARK)
		todoSavable.onSave(todo!!, TodoSavable.Type.STATE)
	}

}

fun interface TodoSavable {
	fun onSave(todo: kanti.tododer.data.model.common.Todo, type: Type)

	enum class Type {
		TITLE,
		REMARK,
		STATE
	}

}
