package kanti.tododer.ui.fragments.components.todo_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.fullId
import kotlin.IllegalArgumentException

abstract class TodoViewHolder(
	todo: Todo,
	private val layoutInflater: LayoutInflater,
	@LayoutRes private val resource: Int,
	private val root: ViewGroup? = null,
	private val attachToRoot: Boolean = false
) {

	private var _view: View? = null
	var eventListener: TodoEventListener? = null
		private set

	var todo: Todo = todo
		set(value) {
			checkType(value.type)
			field = value
			updateView()
		}

	val fullId: String
		get() = todo.fullId

	abstract val type: Todo.Type

	abstract fun bindData(view: View, todo: Todo)

	fun setEventListener(eventListener: TodoEventListener? = null) {
		this.eventListener = eventListener
	}

	val view: View
		get() {
			if (_view == null) {
				_view = createView()
				bindData(_view!!, todo)
			}
			return _view!!
		}

	protected fun onEvent(type: Int, todo: Todo, value: Any? = null) {
		eventListener?.onEvent(type, todo, value)
	}

	private fun createView(): View = layoutInflater.inflate(
		resource,
		root,
		attachToRoot
	).apply {
		setOnClickListener {
			onEvent(EVENT_ON_CLICK, todo)
		}
	}

	private fun updateView() {
		_view?.let { view ->
			checkType(todo.type)
			bindData(view, todo)
		}
	}

	private fun checkType(type: Todo.Type) {
		if (type != this.type)
			throw IllegalArgumentException("Todo element type error. " +
					"Expected: ${this.type}, actual: $type")
	}


	companion object {

		const val EVENT_ON_CLICK = -1

		fun newInstance(
			todo: Todo,
			layoutInflater: LayoutInflater,
			root: ViewGroup? = null,
			attachToRoot: Boolean = false
		): TodoViewHolder {
			return when (todo.type) {
				Todo.Type.TASK -> {
					TaskViewHolder(todo, layoutInflater, root, attachToRoot)
				}
				Todo.Type.PLAN -> {
					PlanViewHolder(todo, layoutInflater, root, attachToRoot)
				}
			}
		}


	}

}