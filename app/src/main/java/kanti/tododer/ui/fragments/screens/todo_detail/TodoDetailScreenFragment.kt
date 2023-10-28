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
import kanti.tododer.data.model.common.fullId
import kanti.tododer.databinding.FragmentTodoDetailBinding
import kanti.tododer.ui.fragments.components.todo_data.viewmodel.TodoDataViewModel
import kanti.tododer.ui.fragments.components.todo_list.viewmodel.TodoListViewModel
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

					when (uiState.type) {
						is TodoDetailUiState.Type.Empty -> {}
						is TodoDetailUiState.Type.Success -> {}
						is TodoDetailUiState.Type.EmptyStack -> { back() }
						is TodoDetailUiState.Type.InvalidFullId -> { toastAndBack(R.string.invalid_data, uiState) }
						is TodoDetailUiState.Type.NotFound -> { toastAndBack(R.string.not_found, uiState) }
						else -> { toastAndBack(R.string.unexpected_error, uiState) }
					}
				}
			}
		}

		viewLifecycleOwner.lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				todoListViewModel.onTaskIsDone.collectLatest { taskDone ->
					viewModel.taskIsDone(taskDone.task, taskDone.done, taskDone.callback)
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

		viewLifecycleOwner.lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				todoListViewModel.onPlanProgressRequest.collectLatest { progressRequest ->
					viewModel.planProgressRequest(progressRequest.plan, progressRequest.callback)
				}
			}
		}
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

	private fun toastAndBack(@StringRes resId: Int, uiState: TodoDetailUiState) {
		val mess = getString(resId)
		Toast.makeText(
			requireContext(),
			"$mess: ${uiState.type.message}",
			Toast.LENGTH_SHORT
		).show()
		back(1000L)
	}

}