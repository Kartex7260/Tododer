package kanti.tododer.ui.fragments.components.todo_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kanti.lifecyclelogger.LifecycleLogger
import kanti.tododer.common.hashLogTag
import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.ui.fragments.components.todo_list.viewmodel.TodoListViewModel
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.toTask
import kanti.tododer.databinding.FragmentTodoListBinding

@AndroidEntryPoint
class TodoListFragment : Fragment() {

	private var _view: FragmentTodoListBinding? = null
	private val view: FragmentTodoListBinding get() { return _view!! }

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
		if (_view == null) {
			_view = FragmentTodoListBinding.inflate(inflater, container, false)
			viewHolderManager = TodoViewHolderManager(
				inflater,
				view.linearLayoutChildren
			)
		}
		return view.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		Log.d(hashLogTag, "onViewCreated(): subscribe to viewModel.todoListLiveData")
		viewModel.todoListLiveData.observe(viewLifecycleOwner) { elements ->
			Log.d(hashLogTag, "onViewCreated(): get observe notification=$elements")
			this.view.linearLayoutChildren.apply {
				removeAllViews()
				for (todoElement in elements) {
					val viewHolder = getViewHolder(todoElement)
					addView(viewHolder.view)
				}
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		Log.d(hashLogTag, "onDestroyView(): unsubscribe from viewModel.todoListLiveData")
		viewModel.todoListLiveData.removeObservers(viewLifecycleOwner)
	}

	private fun getViewHolder(todo: Todo): TodoViewHolder {
		fun log(mes: String) = Log.d(hashLogTag, "getViewHolder(Todo = $todo): $mes")

		val viewHolder = viewHolderManager.getViewHolder(todo)
		val eventListener = object : TodoEventListener {
			override fun onEvent(type: Int, todo: Todo, value: Any?) {
				when (type) {
					TodoViewHolder.EVENT_ON_CLICK -> {
						log("viewHolder.setEventListenerIfNull=${hashCode()} {\n" +
								"\tonEvent: EVENT_ON_CLICK\n}")
						viewModel.elementClick(todo)
					}
					TaskViewHolder.EVENT_IS_DONE -> {
						log("viewHolder.setEventListenerIfNull=${hashCode()} {\n" +
								"\tonEvent: EVENT_IS_DONE\n}")
						eventTaskIsDone(viewHolder, todo, value as Boolean)
					}
				}
			}
		}
		log("set event listener if null")
		viewHolder.setEventListenerIfNull(eventListener)
		return viewHolder
	}

	private fun eventTaskIsDone(viewHolder: TodoViewHolder, todo: Todo, isDone: Boolean) {
		val callbackLiveData = viewModel.taskIsDone(todo.toTask, isDone)
		callbackLiveData.observe(viewLifecycleOwner) { uiState ->
			if (uiState.type != RepositoryResult.Type.Success) {
				viewHolderManager.remove(todo)
				return@observe
			}
			viewHolder.todo = uiState.value
			callbackLiveData.removeObservers(viewLifecycleOwner)
		}
	}

}