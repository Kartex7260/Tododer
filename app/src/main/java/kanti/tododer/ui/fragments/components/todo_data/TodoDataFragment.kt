package kanti.tododer.ui.fragments.components.todo_data

import android.os.Bundle
import android.text.Editable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kanti.tododer.data.common.isSuccess
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task
import kanti.tododer.databinding.FragmentTodoDataBinding
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.toPlan
import kanti.tododer.data.model.common.toTask
import kanti.tododer.ui.common.viewholder.PlanStateViewHolder
import kanti.tododer.ui.common.viewholder.TaskStateViewHolder
import kanti.tododer.ui.common.viewholder.TodoStateViewHolderFactory
import kanti.tododer.ui.common.viewholder.TodoViewHolder
import kanti.tododer.ui.common.viewholder.TodoViewHolderManager
import kanti.tododer.ui.fragments.components.todo_data.viewmodel.TodoDataViewModel
import kanti.tododer.ui.fragments.components.todo_list.setEventListenerIfNull
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoDataFragment : Fragment() {

	private val marginStartTitleEditText: Int get() {
		return TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP,
			16f,
			resources.displayMetrics
		).toInt()
	}

	private var _view: FragmentTodoDataBinding? = null
	private val view: FragmentTodoDataBinding get() { return _view!! }

	private val viewModel: TodoDataViewModel by viewModels(ownerProducer = {
		requireParentFragment()
	})

	private lateinit var todoStateViewHolderManager: TodoViewHolderManager

	private val editableFactory = Editable.Factory.getInstance()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		if (_view == null) {
			_view = FragmentTodoDataBinding.inflate(layoutInflater, container, false)
			todoStateViewHolderManager = TodoViewHolderManager(
				TodoStateViewHolderFactory,
				inflater
			)
		}
		return view.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewLifecycleOwner.lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.todoElement.collectLatest { uiState ->
					showData(uiState.todoElement)
				}
			}
		}

		viewLifecycleOwner.lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.updateStateView.collectLatest {
					todoStateViewHolderManager.current?.also { viewHolder ->
						viewHolder.todo = viewHolder.todo
					}
				}
			}
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

		val todoStateViewHolder = todoStateViewHolderManager.getViewHolder(plan, setCurrent = true)
		todoStateViewHolder.setEventListenerIfNull { type, todo, _, callback ->
			if (type != PlanStateViewHolder.EVENT_PROGRESS_REQUEST)
				return@setEventListenerIfNull

			val callbackLiveData = viewModel.planProgressRequest(todo.toPlan)
			callbackLiveData.observe(viewLifecycleOwner) { progress ->
				callback?.callback(progress)
			}
		}

		hideTodoDataState()
		showTodoDataState(todoStateViewHolder)
	}

	private fun showTask(task: Task) {
		showBasicData(task.title, task.remark)

		val todoStateViewHolder = todoStateViewHolderManager.getViewHolder(task, setCurrent = true)
		todoStateViewHolder.setEventListenerIfNull { type, todo, value, callback ->
			if (type != TaskStateViewHolder.EVENT_IS_DONE)
				return@setEventListenerIfNull

			val callbackLiveData = viewModel.taskIsDone(todo.toTask, value as Boolean)
			callbackLiveData.observe(viewLifecycleOwner) { uiState ->
				callback?.callback(uiState.value)
				callbackLiveData.removeObservers(viewLifecycleOwner)
			}
		}

		hideTodoDataState()
		showTodoDataState(todoStateViewHolder)
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

	private fun showTodoDataState(todoStateView: TodoViewHolder) {
		view.linearLayoutTodoDataStateView.apply {
			addView(todoStateView.view)
		}
	}

	private fun hideTodoDataState() {
		view.linearLayoutTodoDataStateView.apply {
			removeAllViews()
		}
	}

}