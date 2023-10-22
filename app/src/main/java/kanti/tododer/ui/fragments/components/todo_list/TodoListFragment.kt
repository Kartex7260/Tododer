package kanti.tododer.ui.fragments.components.todo_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.databinding.FragmentTodoListBinding
import kanti.tododer.ui.fragments.components.todo_list.viewmodel.TodoListViewModel
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.toTask

@AndroidEntryPoint
class TodoListFragment : Fragment() {

	private lateinit var view: FragmentTodoListBinding
	private val viewModel: TodoListViewModel by viewModels(ownerProducer = {
		requireParentFragment()
	})

	private lateinit var viewHolderManager: TodoViewHolderManager

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		view = FragmentTodoListBinding.inflate(inflater, container, false)
		viewHolderManager = TodoViewHolderManager(
			inflater,
			view.linearLayoutChildren
		)
		return view.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.todoListLiveData.observe(viewLifecycleOwner) { elements ->
			this.view.linearLayoutChildren.apply {
				removeAllViews()
				for (todoElement in elements) {
					val viewHolder = getViewHolder(todoElement)
					addView(viewHolder.view)
				}
			}
		}
	}

	private fun getViewHolder(todoElement: Todo): TodoViewHolder {
		val viewHolder = viewHolderManager.getViewHolder(todoElement)
		viewHolder.setEventListenerIfNull { type, todo, value ->
			when (type) {
				TodoViewHolder.EVENT_ON_CLICK -> viewModel.elementClick(todo)
				TaskViewHolder.EVENT_IS_DONE -> eventTaskIsDone(viewHolder, todo, value as Boolean)
			}
		}
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