package kanti.tododer.ui.fragments.screens.todo_archive_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kanti.tododer.databinding.FragmentScreenTodoArchiveListBinding
import kanti.tododer.ui.fragments.components.todo_list.viewmodel.TodoListViewModel
import kanti.tododer.ui.fragments.screens.todo_archive_list.viewholder.ArchiveTodoViewHolderFactory
import kanti.tododer.ui.fragments.screens.todo_archive_list.viewmodel.ArchiveTodoListViewModel

@AndroidEntryPoint
class ArchiveTodoListScreenFragment : Fragment() {

	private var _viewBinding: FragmentScreenTodoArchiveListBinding? = null
	private val viewBinding: FragmentScreenTodoArchiveListBinding get() = _viewBinding!!

	private val viewModel: ArchiveTodoListViewModel by viewModels()
	private val todoListViewModel: TodoListViewModel by viewModels()

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
	}

	override fun onResume() {
		super.onResume()
		viewModel.getArchivePlans()
	}

}