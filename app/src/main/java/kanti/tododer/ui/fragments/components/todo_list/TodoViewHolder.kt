package kanti.tododer.ui.fragments.components.todo_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import kanti.tododer.ui.state.TodoElement
import kanti.tododer.ui.state.fullId
import kotlin.IllegalArgumentException

abstract class TodoViewHolder(
	todo: TodoElement,
	private val layoutInflater: LayoutInflater,
	@LayoutRes private val resource: Int,
	private val root: ViewGroup? = null,
	private val attachToRoot: Boolean = false
) {

	private var _view: View? = null
	var eventListener: TodoEventListener? = null
		private set

	var todoElement: TodoElement = todo
		set(value) {
			checkType(value.type)
			field = value
			updateView()
		}

	val fullId: String
		get() = todoElement.fullId

	abstract val type: TodoElement.Type

	abstract fun bindData(view: View, todo: TodoElement)

	fun setEventListener(eventListener: TodoEventListener? = null) {
		this.eventListener = eventListener
	}

	val view: View
		get() {
			if (_view == null) {
				_view = createView()
				bindData(_view!!, todoElement)
			}
			return _view!!
		}

	protected fun onEvent(type: Int, todo: TodoElement, value: Any? = null) {
		eventListener?.onEvent(type, todo, value)
	}

	private fun createView(): View = layoutInflater.inflate(
		resource,
		root,
		attachToRoot
	).apply {
		setOnClickListener {
			onEvent(EVENT_ON_CLICK, todoElement)
		}
	}

	private fun updateView() {
		_view?.let { view ->
			checkType(todoElement.type)
			bindData(view, todoElement)
		}
	}

	private fun checkType(type: TodoElement.Type) {
		if (type != this.type)
			throw IllegalArgumentException("Todo element type error. " +
					"Expected: ${this.type}, actual: $type")
	}


	companion object {

		const val EVENT_ON_CLICK = -1

		fun newInstance(
			todo: TodoElement,
			layoutInflater: LayoutInflater,
			root: ViewGroup? = null,
			attachToRoot: Boolean = false
		): TodoViewHolder {
			return when (todo.type) {
				TodoElement.Type.TASK -> {
					TaskViewHolder(todo, layoutInflater, root, attachToRoot)
				}
				TodoElement.Type.PLAN -> {
					PlanViewHolder(todo, layoutInflater, root, attachToRoot)
				}
			}
		}


	}

}