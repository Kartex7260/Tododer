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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kanti.tododer.R
import kanti.tododer.common.Const
import kanti.tododer.common.hashLogTag
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.task.asTask
import kanti.tododer.databinding.FragmentTodoDetailBinding
import kanti.tododer.ui.common.fabowner.setActivityFabOnClickListener
import kanti.tododer.ui.common.toolbarowner.requireActivityToolbar
import kanti.tododer.ui.common.toolbarowner.setActivityToolbar
import kanti.tododer.ui.fragments.common.observe
import kanti.tododer.ui.fragments.components.todo_data.viewmodel.TodoDataViewModel
import kanti.tododer.ui.fragments.components.todo_list.viewmodel.TodoListViewModel
import kanti.tododer.ui.fragments.dialog.TodoSelectorDialogFragment
import kanti.tododer.ui.fragments.screens.todo_detail.viewmodel.TodoSavedUiState
import kanti.tododer.ui.fragments.screens.todo_detail.viewmodel.TodoDetailViewModel
import kanti.tododer.ui.fragments.screens.todo_detail.viewmodel.TodoDetailUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoDetailScreenFragment : Fragment() {

	private lateinit var view: FragmentTodoDetailBinding
	private val viewModel: TodoDetailViewModel by viewModels()
	private val todoListViewModel: TodoListViewModel by viewModels()
	private val todoDataViewModel: TodoDataViewModel by viewModels()

	private val menuProvider by lazy {
		TodoDetailMenuProvider(
			navController = findNavController(),
			settingsNavDirections = TodoDetailScreenFragmentDirections.actionDetailToPreferences(),
			delete = {
				viewModel.deleteTodo()
			}
		)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		requireArguments().apply {
			val planFullId = getString(Const.NAVIGATION_ARGUMENT_FULL_ID) as String
			viewModel.showTodo(planFullId)
		}
	}

	override fun onResume() {
		super.onResume()
		viewModel.reshowTodo()
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

		setActivityToolbar(
			title = R.string.todo,
			defTitle = R.string.app_name,
			navIcon = R.drawable.baseline_arrow_back_24,
			lifecycleOwner = viewLifecycleOwner,
			menuProvider = menuProvider
		) {
			viewModel.pop()
		}

		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
			viewModel.pop()
		}

		setActivityFabOnClickListener {
			viewModel.currentTodoType?.apply {
				when (this) {
					kanti.tododer.data.model.common.Todo.Type.TASK -> {
						viewModel.createNewTask()
					}
					kanti.tododer.data.model.common.Todo.Type.PLAN -> {
						val todoSelectorDialog = TodoSelectorDialogFragment()
						todoSelectorDialog.setTodoSelectListener { type ->
							when (type) {
								kanti.tododer.data.model.common.Todo.Type.TASK -> viewModel.createNewTask()
								kanti.tododer.data.model.common.Todo.Type.PLAN -> viewModel.createNewPlan()
							}
						}
						todoSelectorDialog.show(childFragmentManager, "todo_select")
					}
				}
			}
		}

		observe(viewModel.newTodoCreated) { uiState ->
			when(uiState) {
				is TodoSavedUiState.Success -> {
					viewModel.showTodo(uiState.todo)
				}
				is TodoSavedUiState.NoTodoInstalled -> {
					toastAndBack(R.string.no_todo_installed)

				}
				is TodoSavedUiState.Fail -> {
					toastAndBack(R.string.unexpected_error, uiState.message)
				}
			}
		}

		observe(viewModel.todoDetail) { uiState ->
			showProcess(false)
			when (uiState) {
				is TodoDetailUiState.Empty -> {}
				is TodoDetailUiState.Process -> {
					showProcess(true)
				}
				is TodoDetailUiState.Success -> {
					requireActivityToolbar().apply {
						title = when (uiState.todo.type) {
							kanti.tododer.data.model.common.Todo.Type.TASK -> getString(R.string.task)
							kanti.tododer.data.model.common.Todo.Type.PLAN -> getString(R.string.plan)
						}
					}
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
				}
				is TodoDetailUiState.EmptyStack -> {
					back()
				}
				is TodoDetailUiState.InvalidFullId -> {
					toastAndBack(R.string.invalid_data)
				}
				is TodoDetailUiState.NotFound -> {
					toastAndBack(R.string.not_found, uiState.id)
				}
				is TodoDetailUiState.Fail -> {
					toastAndBack(R.string.unexpected_error, uiState.message)
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

	private fun toastAndBack(@StringRes resId: Int, message: String? = null) {
		val mess = getString(resId)
		Toast.makeText(
			requireContext(),
			"$mess${if (message!=null) ": $message" else ""}",
			Toast.LENGTH_SHORT
		).show()
		back(1000L)
	}

	private fun observeTodoDataFragment() {
		observe(todoDataViewModel.onTaskIsDone) { taskDone ->
			viewModel.taskIsDone(taskDone.todo.asTask, taskDone.data)
		}

		observe(todoDataViewModel.onPlanProgressRequest) { progressRequest ->
			viewModel.planProgressRequest(progressRequest.plan, progressRequest.callback)
		}

		observe(todoDataViewModel.onSaveNewTitle) { titleRequest ->
			viewModel.saveTitle(titleRequest.todo, titleRequest.data)
		}

		observe(todoDataViewModel.onSaveNewRemark) { remarkRequest ->
			viewModel.saveRemark(remarkRequest.todo, remarkRequest.data)
		}
	}

	private fun observeTodoListFragment() {
		observe(todoListViewModel.onTaskIsDone) { taskDone ->
			viewModel.taskIsDone(taskDone.todo.asTask, taskDone.data)
			todoDataViewModel.updateStateView()
		}

		observe(todoListViewModel.onElementClick) {
			viewModel.showTodo(it)
		}

		observe(todoListViewModel.onPlanProgressRequest) { progressRequest ->
			viewModel.planProgressRequest(progressRequest.plan, progressRequest.callback)
		}

		observe(todoListViewModel.onDeleteTodo) { deleteRequest ->
			viewModel.deleteTodo(deleteRequest.todo)
		}
	}

}