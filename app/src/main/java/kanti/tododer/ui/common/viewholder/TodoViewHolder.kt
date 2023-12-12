package kanti.tododer.ui.common.viewholder

import android.annotation.SuppressLint
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import kanti.tododer.R
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.asPlan
import kanti.tododer.data.task.Task
import kanti.tododer.data.task.asTask
import kotlin.IllegalArgumentException

abstract class TodoViewHolder(
	todo: kanti.tododer.data.model.common.Todo,
	private val layoutInflater: LayoutInflater,
	@LayoutRes private val resource: Int,
	private val root: ViewGroup? = RootDefault,
	private val attachToRoot: Boolean = AttachToRootDefault
) {

	private var _view: View? = null
	var eventListener: TodoEventListener? = null
		private set

	private var _todo: kanti.tododer.data.model.common.Todo
	var todo: kanti.tododer.data.model.common.Todo
		get() = _todo
		set(value) {
			checkType(value.type)
			_todo = value
			bindData(view, _todo)
			updateView()
		}

	val fullId: String
		get() = todo.fullId

	abstract val type: kanti.tododer.data.model.common.Todo.Type

	init {
		_todo = todo
	}

	abstract fun onBindData(view: View, todo: kanti.tododer.data.model.common.Todo)

	private fun bindData(view: View, todo: kanti.tododer.data.model.common.Todo) {
		checkType(todo.type)
		onBindData(view, todo)
	}

	fun setEventListener(eventListener: TodoEventListener? = null) {
		this.eventListener = eventListener
	}

	val view: View
		get() {
			if (_view == null) {
				_view = createView()
				onBindData(_view!!, todo)
			}
			return _view!!
		}

	protected fun event(type: Int, todo: kanti.tododer.data.model.common.Todo, value: Any? = null, callback: TodoEventCallback? = null) {
		eventListener?.onEvent(type, todo, value, callback)
	}

	protected open fun createView(): View = layoutInflater.inflate(
		resource,
		root,
		attachToRoot
	).also { view ->
		view.setOnClickListener {
			event(EVENT_ON_CLICK, todo)
		}
		onViewCreated(view)
	}

	protected open fun onViewCreated(view: View) {
		view.apply {
			setOnCreateContextMenuListener(::createContextMenu)
		}
	}

	protected open fun onCreateContextMenu(menu: ContextMenu, view: View, menuInfo: ContextMenu.ContextMenuInfo?) {}

	private fun createContextMenu(menu: ContextMenu, view: View, menuInfo: ContextMenu.ContextMenuInfo?) {
		menu.add(
			CONTEXT_MENU_TODO_GROUP_ID,
			CONTEXT_MENU_DELETE_ITEM_ID,
			CONTEXT_MENU_DELETE_ORDER,
			view.context.getString(R.string.context_menu_delete)
		).apply {
			setOnMenuItemClickListener {
				event(EVENT_CONTEXT_MENU_DELETE_ON_CLICK, todo, view)
				true
			}
		}
		onCreateContextMenu(menu, view, menuInfo)
		event(EVENT_CONTEXT_MENU_ON_CREATE, todo, menu)
	}

	private fun updateView() {
		_view?.let { view ->
			bindData(view, todo)
		}
	}

	private fun checkType(type: kanti.tododer.data.model.common.Todo.Type) {
		if (type != this.type)
			throw IllegalArgumentException("Todo element type error. " +
					"Expected: ${this.type}, actual: $type")
	}

	interface Factory {

		fun createTaskViewHolder(
			todo: kanti.tododer.data.task.Task,
			layoutInflater: LayoutInflater,
			root: ViewGroup? = RootDefault,
			attachToRoot: Boolean = AttachToRootDefault
		): TodoViewHolder

		fun createPlanViewHolder(
			todo: kanti.tododer.data.model.plan.Plan,
			layoutInflater: LayoutInflater,
			root: ViewGroup? = RootDefault,
			attachToRoot: Boolean = AttachToRootDefault
		): TodoViewHolder

	}

	companion object {

		const val EVENT_ON_CLICK = -1
		const val EVENT_CONTEXT_MENU_DELETE_ON_CLICK = -2
		const val EVENT_CONTEXT_MENU_ON_CREATE = -3

		const val CONTEXT_MENU_TODO_GROUP_ID = 0
		const val CONTEXT_MENU_DELETE_ITEM_ID = 0
		const val CONTEXT_MENU_DELETE_ORDER = 100

		@SuppressLint("StaticFieldLeak")
		val RootDefault: ViewGroup? = null
		const val AttachToRootDefault = false
		const val NonResource = 0

		fun newInstance(
			todoViewHolderFactory: Factory,
			todo: kanti.tododer.data.model.common.Todo,
			layoutInflater: LayoutInflater,
			root: ViewGroup? = null,
			attachToRoot: Boolean = false
		): TodoViewHolder {
			return when (todo.type) {
				kanti.tododer.data.model.common.Todo.Type.TASK -> {
					todoViewHolderFactory.createTaskViewHolder(
						todo.asTask,
						layoutInflater,
						root,
						attachToRoot
					)
				}
				kanti.tododer.data.model.common.Todo.Type.PLAN -> {
					todoViewHolderFactory.createPlanViewHolder(
						todo.asPlan,
						layoutInflater,
						root,
						attachToRoot
					)
				}
			}
		}
	}

}