package kanti.tododer.ui.fragments.components.todo_data

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task
import kanti.tododer.databinding.FragmentTodoDataBinding
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.toPlan
import kanti.tododer.data.model.common.toTask

@AndroidEntryPoint
class TodoDataFragment : Fragment() {

	private lateinit var view: FragmentTodoDataBinding
	private val viewModel: TodoDataViewModel by activityViewModels()

	private val editableFactory = Editable.Factory.getInstance()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		view = FragmentTodoDataBinding.inflate(layoutInflater, container, false)
		return view.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.todoElementLiveData.observe(viewLifecycleOwner) { uiState ->
			showData(uiState.todoElement)
		}
	}

	private fun showData(todoElement: Todo?) {
		if (todoElement == null) {
			clearEditText()
			return
		}

		when (todoElement.type) {
			Todo.Type.TASK -> showTask(todoElement.toTask)
			Todo.Type.PLAN -> showPlan(todoElement.toPlan)
		}
	}

	private fun showPlan(plan: Plan) {
		showBasicData(plan.title, plan.remark)
	}

	private fun showTask(task: Task) {
		showBasicData(task.title, task.remark)
	}

	private fun showBasicData(title: String, remark: String) {
		view.apply {
			editTextTodoDetailTitle.text = editableFactory.newEditable(title)
			editTextTodoDetailRemark.text = editableFactory.newEditable(remark)
		}
	}

	private fun clearEditText() {
		view.apply {
			editTextTodoDetailTitle.text.clear()
			editTextTodoDetailRemark.text.clear()
		}
	}

}