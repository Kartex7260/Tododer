package kanti.tododer.ui.fragments.screens.todo_detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kanti.tododer.R
import kanti.tododer.common.Const
import kanti.tododer.common.hashLogTag
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.fullId
import kanti.tododer.databinding.FragmentTodoDetailBinding
import kanti.tododer.ui.common.fabowner.setActivityFabOnClickListener
import kanti.tododer.ui.fragments.components.todo_data.viewmodel.TodoDataViewModel
import kanti.tododer.ui.fragments.components.todo_list.viewmodel.TodoListViewModel
import kanti.tododer.ui.fragments.dialog.TodoSelectorDialogFragment
import kanti.tododer.ui.fragments.screens.todo_detail.viewmodel.NewTodoCreatedUiState
import kanti.tododer.ui.fragments.screens.todo_detail.viewmodel.TodoDetailViewModel
import kanti.tododer.ui.fragments.screens.todo_detail.viewmodel.TodoDetailUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoDetailScreenFragment : Fragment() {

	private lateinit var view: FragmentTodoDetailBinding
	private val viewModel: TodoDetailViewModel by viewModels()
	private val todoListViewModel: TodoListViewModel by viewModels()
	private val todoDataViewModel: TodoDataViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		requireArguments().apply {
			val planFullId = getString(Const.NAVIGATION_ARGUMENT_FULL_ID) as String
			viewModel.showTodo(planFullId)
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		view = FragmentTodoDetailBinding.inflate(inflater, container, false)
		return view.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
			viewModel.pop()
		}

		setActivityFabOnClickListener {
			viewModel.currentTodoType?.also { type ->
				when (type) {
					Todo.Type.TASK -> {
						viewModel.createNewTask()
					}
					Todo.Type.PLAN -> {
						val todoSelectorDialog = TodoSelectorDialogFragment()
						todoSelectorDialog.setTodoSelectListener { type ->
							when (type) {
								Todo.Type.TASK -> viewModel.createNewTask()
								Todo.Type.PLAN -> viewModel.createNewPlan()
							}
						}
						todoSelectorDialog.show(childFragmentManager, "todo_select")
					}
				}
			}
		}

		viewLifecycleOwner.lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.newTodoCreated.collectLatest { uiState ->
					val `continue` = showMessageFromTodoCreatedUiStateType(uiState.type)
					if (!`continue` && uiState.todo != null)
						return@collectLatest

					viewModel.showTodo(uiState.todo!!)
				}
			}
		}

		viewLifecycleOwner.lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.todoDetail.collectLatest { uiState ->
					showProcess(uiState.process)
					Log.d(
						hashLogTag,
						"onViewCreated(View, Bundle?): todoDataViewModel.sendTodo(Todo=${uiState.todo})\n" +
								"todoDataViewModel=${todoDataViewModel.hashLogTag}"
					)
					todoDataViewModel.sendTodo(uiState.todo)
					Log.d(
						hashLogTag,
						"onViewCreated(View, Bundle?): todoListViewModel.sendTodoList(List<Todo> = ${uiState.todoChildren}\n" +
								"todoListViewModel=${todoListViewModel.hashLogTag}"
					)
					todoListViewModel.sendTodoList(uiState.todoChildren)

					showMessageFromTodoDetailUiStateType(uiState.type)
				}
			}
		}

		observeTodoDataFragment()
		observeTodoListFragment()
	}

	private fun showProcess(process: Boolean) {
		if (process) {
			view.progressBarTodoDetail.visibility = View.VISIBLE
		} else {
			view.progressBarTodoDetail.visibility = View.INVISIBLE
		}
	}

	private fun showMessageFromTodoDetailUiStateType(type: TodoDetailUiState.Type) {
		when (type) {
			is TodoDetailUiState.Type.Empty -> {}
			is TodoDetailUiState.Type.Success -> {}
			is TodoDetailUiState.Type.EmptyStack -> { back() }
			is TodoDetailUiState.Type.InvalidFullId -> { toastAndBack(R.string.invalid_data, type) }
			is TodoDetailUiState.Type.NotFound -> { toastAndBack(R.string.not_found, type) }
			else -> { toastAndBack(R.string.unexpected_error, type) }
		}
	}

	private fun showMessageFromTodoCreatedUiStateType(type: NewTodoCreatedUiState.Type): Boolean {
		return when(type) {
			is NewTodoCreatedUiState.Type.Success -> true
			is NewTodoCreatedUiState.Type.NoTodoInstalled -> {
				toastAndBack(R.string.no_todo_installed, type)
				false
			}
			is NewTodoCreatedUiState.Type.AlreadyExists -> {
				toastAndBack(R.string.already_exist, type)
				false
			}
			is NewTodoCreatedUiState.Type.NotFound -> {
				toastAndBack(R.string.not_found, type)
				false
			}
			is NewTodoCreatedUiState.Type.Fail -> {
				toastAndBack(R.string.unexpected_error, type)
				false
			}
		}
	}

	private fun back(millis: Long? = null) {
		if (millis == null) {
			findNavController().popBackStack()
		} else {
			lifecycleScope.launch {
				delay(millis)
				findNavController().popBackStack()
			}
		}
	}

	private fun toastAndBack(@StringRes resId: Int, type: TodoDetailUiState.Type) {
		val mess = getString(resId)
		Toast.makeText(
			requireContext(),
			"$mess: ${type.message}",
			Toast.LENGTH_SHORT
		).show()
		back(1000L)
	}

	private fun toastAndBack(@StringRes resId: Int, type: NewTodoCreatedUiState.Type) {
		val mess = getString(resId)
		Toast.makeText(
			requireContext(),
			"$mess: ${type.message}",
			Toast.LENGTH_SHORT
		).show()
		back(1000L)
	}

	private fun observeTodoDataFragment() {
		viewLifecycleOwner.lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				todoDataViewModel.onTaskIsDone.collectLatest { taskDone ->
					viewModel.taskIsDone(taskDone.task, taskDone.done, taskDone.callback)
				}
			}
		}

		viewLifecycleOwner.lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				todoDataViewModel.onPlanProgressRequest.collectLatest { progressRequest ->
					viewModel.planProgressRequest(progressRequest.plan, progressRequest.callback)
				}
			}
		}
	}

	private fun observeTodoListFragment() {
		viewLifecycleOwner.lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				todoListViewModel.onTaskIsDone.collectLatest { taskDone ->
					viewModel.taskIsDone(taskDone.task, taskDone.done, taskDone.callback)
					todoDataViewModel.updateStateView()
				}
			}
		}

		viewLifecycleOwner.lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				todoListViewModel.onElementClick.collectLatest {
					viewModel.showTodo(it)
				}
			}
		}

		viewLifecycleOwner.lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				todoListViewModel.onPlanProgressRequest.collectLatest { progressRequest ->
					viewModel.planProgressRequest(progressRequest.plan, progressRequest.callback)
				}
			}
		}

		viewLifecycleOwner.lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				todoListViewModel.onDeleteTodo.collectLatest { progressRequest ->
					viewModel.deletePlan(progressRequest.todo)
				}
			}
		}
	}

}