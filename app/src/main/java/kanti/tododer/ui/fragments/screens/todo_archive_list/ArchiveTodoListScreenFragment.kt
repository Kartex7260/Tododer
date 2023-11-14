package kanti.tododer.ui.fragments.screens.todo_archive_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kanti.tododer.R
import kanti.tododer.data.model.common.Todo
import kanti.tododer.databinding.FragmentScreenTodoArchiveListBinding
import kanti.tododer.ui.common.toolbarowner.setActivityToolbar
import kanti.tododer.ui.fragments.common.observe
import kanti.tododer.ui.fragments.components.todo_list.viewmodel.TodoListUserViewModel
import kanti.tododer.ui.fragments.components.todo_list.viewmodel.TodoListViewModel
import kanti.tododer.ui.fragments.screens.todo_archive_list.viewholder.ArchiveTodoViewHolderFactory
import kanti.tododer.ui.fragments.screens.todo_archive_list.viewmodel.ArchiveTodoListViewModel

@AndroidEntryPoint
class ArchiveTodoListScreenFragment : Fragment() {

	private var _viewBinding: FragmentScreenTodoArchiveListBinding? = null
	private val viewBinding: FragmentScreenTodoArchiveListBinding get() = _viewBinding!!

	private val viewModel: ArchiveTodoListViewModel by viewModels()
	private val todoListViewModel: TodoListUserViewModel by viewModels<TodoListViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		todoListViewModel.todoViewHolderFactory = ArchiveTodoViewHolderFactory
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		if (_viewBinding == null) {
			_viewBinding = FragmentScreenTodoArchiveListBinding.inflate(
				layoutInflater,
				container,
				false
			)
		}
		return viewBinding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setActivityToolbar(
			title = R.string.archive,
			lifecycleOwner =  viewLifecycleOwner
		)

		observe(viewModel.archivePlans) { uiState ->
			showProcess(uiState.process)

			todoListViewModel.sendTodoList(uiState.plans)
		}

		observeTodoListViewModel()
	}

	override fun onResume() {
		super.onResume()
		viewModel.getArchivePlans()
	}

	private fun showProcess(process: Boolean) {
		if (process) {
			viewBinding.progressBarTodoArchive.visibility = View.VISIBLE
		} else {
			viewBinding.progressBarTodoArchive.visibility = View.GONE
		}
	}

	private fun observeTodoListViewModel() {
		observe(todoListViewModel.elementClick) {
			navigateToDetailScreen(it)
		}
		observe(todoListViewModel.todoItemCreateContextMenu) { createMenuRequest ->
			val todo = createMenuRequest.todo
			createMenuRequest.contextMenu.add(
				Menu.NONE,
				1,
				Menu.NONE,
				R.string.unarchive
			).setOnMenuItemClickListener {
				viewModel.unarchiveTodo(todo)
				todoListViewModel.removeTodoView(todo)
				true
			}
		}
	}

	private fun navigateToDetailScreen(todoElement: Todo) {
		val navDirections = ArchiveTodoListScreenFragmentDirections
			.actionArchiveListToArchiveDetail(todoElement.fullId)
		findNavController().navigate(navDirections)
	}

}