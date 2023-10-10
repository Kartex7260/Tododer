package kanti.tododer.ui.screens.screen.todo_detail.components

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kanti.tododer.databinding.FragmentTodoDetailDataBinding
import kanti.tododer.domain.plan.planwithchildren.PlanWithChildren
import kanti.tododer.domain.task.taskwithchildren.TaskWithChildren
import kanti.tododer.ui.screens.screen.todo_detail.viewmodel.TodoDetailViewModel
import kanti.tododer.ui.state.TodoElement

@AndroidEntryPoint
class TodoDetailDataFragment : Fragment() {

	private lateinit var view: FragmentTodoDetailDataBinding
	private val viewModel: TodoDetailViewModel by activityViewModels()

	private val editableFactory = Editable.Factory.getInstance()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		view = FragmentTodoDetailDataBinding.inflate(layoutInflater, container, false)
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
			view.editTextTodoDetailTitle.text.clear()
			view.editTextTodoDetailRemark.text.clear()
			return
		}

		when (todoElement.type) {
			TodoElement.Type.TASK -> showTask(todoElement.toTask)
			TodoElement.Type.PLAN -> showPlan(todoElement.toPlan)
		}
	}

	private fun showPlan(planWithChildren: PlanWithChildren) {
		val plan = planWithChildren.plan ?: return
		showBasicData(plan.title, plan.remark)
	}

	private fun showTask(taskWithChildren: TaskWithChildren) {
		val task = taskWithChildren.task ?: return
		showBasicData(task.title, task.remark)
	}

	private fun showBasicData(title: String, remark: String) {
		view.apply {
			editTextTodoDetailTitle.text = editableFactory.newEditable(title)
			editTextTodoDetailRemark.text = editableFactory.newEditable(remark)
		}
	}

}