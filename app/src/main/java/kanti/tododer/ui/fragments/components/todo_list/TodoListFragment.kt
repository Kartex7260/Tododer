package kanti.tododer.ui.fragments.components.todo_list

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kanti.lifecyclelogger.LifecycleLogger
import kanti.tododer.common.hashLogTag
import kanti.tododer.ui.fragments.components.todo_list.viewmodel.TodoListViewModel
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.asPlan
import kanti.tododer.data.model.common.asTask
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task
import kanti.tododer.databinding.FragmentTodoListBinding
import kanti.tododer.ui.common.viewholder.ItemListTodoViewHolderFactory
import kanti.tododer.ui.common.viewholder.PlanViewHolder
import kanti.tododer.ui.common.viewholder.TaskViewHolder
import kanti.tododer.ui.common.viewholder.TodoEventCallback
import kanti.tododer.ui.common.viewholder.TodoEventListener
import kanti.tododer.ui.common.viewholder.TodoViewHolder
import kanti.tododer.ui.common.viewholder.TodoViewHolderManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoListFragment : Fragment() {

	private var _viewBinding: FragmentTodoListBinding? = null
	private val viewBinding: FragmentTodoListBinding get() { return _viewBinding!! }

	private val viewModel: TodoListViewModel by viewModels(ownerProducer = {
		requireParentFragment()
	})

	private lateinit var viewHolderManager: TodoViewHolderManager

	private lateinit var lifecycleLogger: LifecycleLogger

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		lifecycleLogger = LifecycleLogger(this)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		if (_viewBinding == null) {
			_viewBinding = FragmentTodoListBinding.inflate(inflater, container, false)
			viewHolderManager = TodoViewHolderManager(
				ItemListTodoViewHolderFactory,
				inflater,
				viewBinding.linearLayoutChildren
			)
		}
		return viewBinding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		Log.d(hashLogTag, "onViewCreated(View, Bundle?): subscribe to viewModel.todoList\n" +
				"viewModel=${viewModel.hashLogTag}")
		viewLifecycleOwner.lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.todoList.collectLatest { elements ->
					Log.d(
						hashLogTag,
						"onViewCreated(View, Bundle?): get observe notification=$elements"
					)
					viewBinding.linearLayoutChildren.apply {
						removeAllViews()
						for (todoElement in elements) {
							val viewHolder = getViewHolder(todoElement)
							addView(viewHolder.view)
						}
					}
				}
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		Log.d(hashLogTag, "onDestroyView(): unsubscribe from viewModel.todoListLiveData")
	}

	private fun getViewHolder(todo: Todo): TodoViewHolder {
		fun log(mes: String) = Log.d(hashLogTag, "getViewHolder(Todo = $todo): $mes")

		val viewHolder = viewHolderManager.getViewHolder(todo)
		val eventListener = object : TodoEventListener {
			override fun onEvent(type: Int, todo: Todo, value: Any?, callback: TodoEventCallback?) {
				when (type) {
					TodoViewHolder.EVENT_ON_CLICK -> {
						log("viewHolder.setEventListenerIfNull=${hashCode()} {\n" +
								"\tonEvent: EVENT_ON_CLICK\n}")
						viewModel.elementClick(todo)
					}
					TodoViewHolder.EVENT_CONTEXT_MENU_DELETE_ON_CLICK -> {
						viewModel.deleteTodo(todo)
						viewHolderManager.remove(todo)
						viewBinding.linearLayoutChildren.removeView(viewHolder.view)
					}
					TodoViewHolder.EVENT_CONTEXT_MENU_ON_CREATE -> {
						if (value == null || value !is ContextMenu)
							return
						viewModel.todoItemCreateContextMenu(todo, value)
					}
					else -> {
						when (todo.type) {
							Todo.Type.TASK -> taskEvents(type, todo.asTask, value, callback)
							Todo.Type.PLAN -> planEvents(type, todo.asPlan, value, callback)
						}
					}
				}
			}
		}
		log("set event listener if null")
		viewHolder.setEventListenerIfNull(eventListener)
		return viewHolder
	}

	private fun taskEvents(type: Int, task: Task, value: Any?, callback: TodoEventCallback?) {
		fun log(mes: String) = Log.d(hashLogTag, "taskEvents(type = $type, " +
				"Plan = $task, value = $value, callback = $callback): $mes")

		when (type) {
			TaskViewHolder.EVENT_IS_DONE -> {
				log("viewHolder.setEventListenerIfNull=${hashCode()} {\n" +
						"\tonEvent: EVENT_IS_DONE\n}")
				viewModel.taskIsDone(task, value as Boolean)
			}
		}
	}

	private fun planEvents(type: Int, plan: Plan, value: Any?, callback: TodoEventCallback?) {
		fun log(mes: String) = Log.d(hashLogTag, "planEvents(type = $type, " +
				"Plan = $plan, value = $value, callback = $callback): $mes")

		when (type) {
			PlanViewHolder.EVENT_PROGRESS_REQUEST -> {
				log("viewHolder.setEventListenerIfNull=${hashCode()} {\n" +
						"\tonEvent: EVENT_PROGRESS_REQUEST\n}")
				eventPlanProgressRequest(plan, callback)
			}
		}
	}

	private fun eventPlanProgressRequest(plan: Plan, callback: TodoEventCallback?) {
		val callbackLiveData = viewModel.progressRequest(plan)
		callbackLiveData.observe(viewLifecycleOwner) { progress ->
			callback?.callback(progress)
		}
	}

}