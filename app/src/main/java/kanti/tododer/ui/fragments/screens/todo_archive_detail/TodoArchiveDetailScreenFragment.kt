package kanti.tododer.ui.fragments.screens.todo_archive_detail

import android.os.Bundle
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
import kanti.tododer.databinding.FragmentScreenTodoArchiveDetailBinding
import kanti.tododer.ui.fragments.common.observe
import kanti.tododer.ui.fragments.components.todo_data.viewmodel.TodoDataUserViewModel
import kanti.tododer.ui.fragments.components.todo_data.viewmodel.TodoDataViewModel
import kanti.tododer.ui.fragments.components.todo_list.viewmodel.TodoListUserViewModel
import kanti.tododer.ui.fragments.components.todo_list.viewmodel.TodoListViewModel
import kanti.tododer.ui.fragments.screens.todo_archive_detail.viewmodel.ArchiveTodoDetailUiState
import kanti.tododer.ui.fragments.screens.todo_archive_detail.viewmodel.TodoArchiveDetailViewModel
import kanti.tododer.ui.fragments.screens.todo_archive_list.viewholder.ArchiveTodoViewHolderFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoArchiveDetailScreenFragment : Fragment() {

	private var _viewBinding: FragmentScreenTodoArchiveDetailBinding? = null
	private val viewBinding: FragmentScreenTodoArchiveDetailBinding get() = _viewBinding!!

	private val todoDataViewModeL: TodoDataUserViewModel by viewModels<TodoDataViewModel>()
	private val todoListViewModel: TodoListUserViewModel by viewModels<TodoListViewModel>()
	private val viewModel: TodoArchiveDetailViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		todoListViewModel.todoViewHolderFactory = ArchiveTodoViewHolderFactory
		requireArguments().apply {
			val fullId = getString(Const.NAVIGATION_ARGUMENT_FULL_ID) as String
			viewModel.showTodo(fullId)
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		if (_viewBinding == null) {
			_viewBinding = FragmentScreenTodoArchiveDetailBinding
				.inflate(inflater, container, false)
		}
		return viewBinding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
			viewModel.pop()
		}

		observe(viewModel.currentTodo) { uiState ->
			showProcess(uiState.process)
			showType(uiState.type)
			todoDataViewModeL.sendTodo(uiState.todo)
			todoListViewModel.sendTodoList(uiState.todoChildren)
		}
	}

	override fun onResume() {
		super.onResume()
		viewModel.reshowTodo()
	}

	private fun toastAndPopBack(@StringRes stg: Int) {
		Toast.makeText(requireContext(), stg, Toast.LENGTH_SHORT).show()
		viewLifecycleOwner.lifecycleScope.launch {
			delay(1000L)
			findNavController().popBackStack()
		}
	}

	private fun showType(type: ArchiveTodoDetailUiState.Type) {
		when (type) {
			is ArchiveTodoDetailUiState.Type.Empty -> {}
			is ArchiveTodoDetailUiState.Type.SuccessLocal -> {}
			is ArchiveTodoDetailUiState.Type.NotFound -> {
				toastAndPopBack(R.string.not_found)
			}
			is ArchiveTodoDetailUiState.Type.InvalidFullId -> {
				toastAndPopBack(R.string.invalid_data)
			}
			is ArchiveTodoDetailUiState.Type.EmptyStack -> {
				findNavController().popBackStack()
			}
			else -> {
				toastAndPopBack(R.string.unexpected_error)
			}
		}
	}

	private fun showProcess(process: Boolean) {
		if (process) {
			viewBinding.progressBarTodoArchiveDetail.visibility = View.VISIBLE
		} else {
			viewBinding.progressBarTodoArchiveDetail.visibility = View.GONE
		}
	}

}