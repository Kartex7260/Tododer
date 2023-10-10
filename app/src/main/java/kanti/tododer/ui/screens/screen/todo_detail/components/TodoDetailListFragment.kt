package kanti.tododer.ui.screens.screen.todo_detail.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withResumed
import dagger.hilt.android.AndroidEntryPoint
import kanti.tododer.R
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task
import kanti.tododer.databinding.FragmentTodoDetailListBinding
import kanti.tododer.ui.screens.screen.todo_detail.viewmodel.TodoDetailViewModel
import kanti.tododer.ui.screens.screen.todo_detail.viewmodel.uistate.TodoElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoDetailListFragment : Fragment() {

	private lateinit var view: FragmentTodoDetailListBinding
	private val viewModel: TodoDetailViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		view = FragmentTodoDetailListBinding.inflate(inflater, container, false)
		return view.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.todoDetailLiveDataLiveData.observe(viewLifecycleOwner) { uiState ->
			showData(uiState.todo)
		}
	}

	private fun showData(todoElement: TodoElement?) {
		if (todoElement == null) {
			view.linearLayoutChildren.removeAllViews()
			return
		}

		when (todoElement.type) {
			TodoElement.Type.TASK -> {
				showTasks(todoElement.toTask.childTasks)
			}
			TodoElement.Type.PLAN -> {
				todoElement.toPlan.apply {
					showPlansAndTasks(childPlans, childTasks)
				}
			}
		}
	}

	private fun showTasks(tasks: List<Task>) {
		lifecycleScope.launch(Dispatchers.Default) {
			tasks.asFlow()
				.collect { task ->
					showTask(task)
				}
		}
	}

	private fun showPlansAndTasks(plans: List<Plan>, tasks: List<Task>) {
		lifecycleScope.launch(Dispatchers.Default) {
			plans.asFlow()
				.collect { plan ->
					showPlan(plan)
				}
			showTasks(tasks)
		}
	}

	private suspend fun showPlan(plan: Plan) {
		val cardView = layoutInflater.inflate(R.layout.view_plan_list_item, null)
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
		withResumed {
			view.linearLayoutChildren.addView(cardView)
		}
	}

	private suspend fun showTask(task: Task) {
		val cardView = layoutInflater.inflate(R.layout.view_task_list_item, null)
		cardView.findViewById<TextView>(R.id.textViewListItemTaskTitle).apply {
			text = task.title
		}
		withResumed {
			view.linearLayoutChildren.addView(cardView)
		}
	}

}