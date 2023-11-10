package kanti.tododer.ui.fragments.components.todo_data

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task
import kanti.tododer.databinding.FragmentTodoDataBinding
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.asPlan
import kanti.tododer.data.model.task.asTask
import kanti.tododer.ui.common.viewholder.PlanStateViewHolder
import kanti.tododer.ui.common.viewholder.TaskStateViewHolder
import kanti.tododer.ui.common.viewholder.TaskStateViewOwner
import kanti.tododer.ui.common.viewholder.TodoStateViewHolderFactory
import kanti.tododer.ui.common.viewholder.TodoViewHolder
import kanti.tododer.ui.common.viewholder.TodoViewHolderManager
import kanti.tododer.ui.fragments.common.observe
import kanti.tododer.ui.fragments.components.todo_data.viewmodel.TodoDataViewModel
import kanti.tododer.ui.fragments.components.todo_data.viewmodel.TodoSavable
import kanti.tododer.ui.fragments.components.todo_list.setEventListenerIfNull

@AndroidEntryPoint
class TodoDataFragment : Fragment() {

	private val editTitleTextChangeListener: (Editable?) -> Unit = { editable ->
		editable?.apply {
			viewModel.saveNewTitle(toString())
		}
	}
	private val editRemarkTextChangeListener: (Editable?) -> Unit = { editable ->
		editable?.apply {
			viewModel.saveNewRemark(toString())
		}
	}

	private var _viewBinding: FragmentTodoDataBinding? = null
	private val viewBinding: FragmentTodoDataBinding get() { return _viewBinding!! }
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
		if (_viewBinding == null) {
			_viewBinding = FragmentTodoDataBinding.inflate(layoutInflater, container, false)
			todoStateViewHolderManager = TodoViewHolderManager(
				TodoStateViewHolderFactory,
				inflater
			)
		}
		return viewBinding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.setTodoDataSaveObserver(viewLifecycleOwner) { _, type ->
			when (type) {
				TodoSavable.Type.TITLE -> {
					val title = viewBinding.editTextTodoDetailTitle.text.toString()
					viewModel.saveNewTitle(title)
				}
				TodoSavable.Type.REMARK -> {
					val remark = viewBinding.editTextTodoDetailRemark.text.toString()
					viewModel.saveNewRemark(remark)
				}
				TodoSavable.Type.STATE -> {
					val viewHolder = todoStateViewHolderManager.current ?: return@setTodoDataSaveObserver
					if (viewHolder !is TaskStateViewOwner)
						return@setTodoDataSaveObserver
					when (viewHolder.todo.type) {
						Todo.Type.TASK -> {
							viewModel.taskIsDone(
								viewHolder.todo.asTask,
								viewHolder.stateView.isChecked
							)
						}
						else -> {}
					}
				}
			}
		}

		observe(viewModel.todoElement) { uiState ->
			viewBinding.unsubscribeTextFields()
			showData(uiState.todoElement)
			viewBinding.subscribeTextFields()
		}

		observe(viewModel.updateStateView) {
			todoStateViewHolderManager.current?.also { viewHolder ->
				viewHolder.todo = viewHolder.todo
			}
		}
	}

	private fun FragmentTodoDataBinding.subscribeTextFields() {
		editTextTodoDetailTitle.addTextChangedListener(afterTextChanged = editTitleTextChangeListener)
		editTextTodoDetailRemark.addTextChangedListener(afterTextChanged = editRemarkTextChangeListener)
	}

	private fun FragmentTodoDataBinding.unsubscribeTextFields() {
		editTextTodoDetailTitle.addTextChangedListener()
		editTextTodoDetailRemark.addTextChangedListener()
	}

	private fun showData(todoElement: Todo?) {
		if (todoElement == null) {
			clearEditText()
			return
		}

		when (todoElement.type) {
			Todo.Type.TASK -> showTask(todoElement.asTask)
			Todo.Type.PLAN -> showPlan(todoElement.asPlan)
		}
	}

	private fun showPlan(plan: Plan) {
		showBasicData(plan.title, plan.remark)

		val todoStateViewHolder = todoStateViewHolderManager.getViewHolder(plan, setCurrent = true)
		todoStateViewHolder.setEventListenerIfNull { type, todo, _, callback ->
			if (type != PlanStateViewHolder.EVENT_PROGRESS_REQUEST)
				return@setEventListenerIfNull

			val callbackLiveData = viewModel.planProgressRequest(todo.asPlan)
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
		todoStateViewHolder.setEventListenerIfNull { type, todo, value, _ ->
			if (type != TaskStateViewHolder.EVENT_IS_DONE)
				return@setEventListenerIfNull

			viewModel.taskIsDone(todo.asTask, value as Boolean)
		}

		hideTodoDataState()
		showTodoDataState(todoStateViewHolder)
	}

	private fun showBasicData(title: String, remark: String) {
		viewBinding.apply {
			editTextTodoDetailTitle.text = editableFactory.newEditable(title)
			editTextTodoDetailRemark.text = editableFactory.newEditable(remark)
		}
	}

	private fun clearEditText() {
		viewBinding.apply {
			editTextTodoDetailTitle.text.clear()
			editTextTodoDetailRemark.text.clear()
		}
	}

	private fun showTodoDataState(todoStateView: TodoViewHolder) {
		viewBinding.linearLayoutTodoDataStateView.apply {
			addView(todoStateView.view)
		}
	}

	private fun hideTodoDataState() {
		viewBinding.linearLayoutTodoDataStateView.apply {
			removeAllViews()
		}
	}

}