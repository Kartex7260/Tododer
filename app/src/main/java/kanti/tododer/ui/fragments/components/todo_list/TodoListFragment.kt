package kanti.tododer.ui.fragments.components.todo_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kanti.tododer.databinding.FragmentTodoListBinding
import kanti.tododer.ui.fragments.components.todo_list.viewmodel.TodoListViewModel
import kanti.tododer.ui.state.TodoElement

@AndroidEntryPoint
class TodoListFragment : Fragment() {

	private lateinit var view: FragmentTodoListBinding
	private val viewModel: TodoListViewModel by activityViewModels()

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

	private fun getViewHolder(todoElement: TodoElement): TodoViewHolder {
		return viewHolderManager.getViewHolder(todoElement).apply {
			setEventListenerIfNull { type, todo, value ->
				when (type) {
					TodoViewHolder.EVENT_ON_CLICK -> viewModel.elementClick(todo)
					TaskViewHolder.EVENT_DONE -> viewModel.taskIsDone(todo.toTask, value as Boolean)
				}
			}
		}
	}

}