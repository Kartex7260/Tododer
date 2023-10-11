package kanti.tododer.ui.fragments.components.todo_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kanti.tododer.R
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task
import kanti.tododer.databinding.FragmentTodoListBinding
import kanti.tododer.ui.state.TodoElement

typealias OnTodoElementClick = (TodoElement) -> Unit

@AndroidEntryPoint
class TodoListFragment : Fragment() {

	private lateinit var view: FragmentTodoListBinding
	private val viewModel: TodoListViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		view = FragmentTodoListBinding.inflate(inflater, container, false)
		return view.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.todoListLiveData.observe(viewLifecycleOwner) { uiState ->
			showTodoElements(
				uiState.todoElements,
				uiState.onItemClick
			)
		}
	}

	private fun showTodoElements(
		todoElements: List<TodoElement>,
		onElementClick: OnTodoElementClick
	) {
		view.linearLayoutChildren.removeAllViews()
		for (todoElement in todoElements) {
			showTodoElement(
				todoElement,
				onElementClick
			)
		}
	}

	private fun showTodoElement(
		todoElement: TodoElement,
		onElementClick: OnTodoElementClick
	) {
		when (todoElement.type) {
			TodoElement.Type.TASK -> {
				showTask(
					todoElement,
					onElementClick
				)
			}
			TodoElement.Type.PLAN -> {
				showPlan(
					todoElement,
					onElementClick
				)
			}
		}
	}

	private fun showPlan(
		todoElement: TodoElement,
		onElementClick: OnTodoElementClick
	) {
		val cardView = layoutInflater.inflate(
			R.layout.view_plan_list_item,
			view.root,
			false
		).apply {
			setOnClickListener {
				onElementClick(todoElement)
			}
		}

		val plan = todoElement.toPlan

		cardView.findViewById<TextView>(R.id.textViewListItemPlanTitle).apply {
			text = plan.title
		}
		cardView.findViewById<TextView>(R.id.textViewListItemPlanRemark).apply {
			if (plan.remark.isBlank()) {
				visibility = View.GONE
				return@apply
			}
			text = plan.remark
		}
		view.linearLayoutChildren.addView(cardView)
	}

	private fun showTask(
		todoElement: TodoElement,
		onElementClick: OnTodoElementClick
	) {
		val cardView = layoutInflater.inflate(
			R.layout.view_task_list_item,
			view.root,
			false
		).apply {
			setOnClickListener {
				onElementClick(todoElement)
			}
		}

		val task = todoElement.toTask

		cardView.findViewById<TextView>(R.id.textViewListItemTaskTitle).apply {
			text = task.title
		}
		view.linearLayoutChildren.addView(cardView)
	}

}