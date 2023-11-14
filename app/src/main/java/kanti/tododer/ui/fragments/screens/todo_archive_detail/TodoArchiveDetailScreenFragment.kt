package kanti.tododer.ui.fragments.screens.todo_archive_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kanti.tododer.common.Const
import kanti.tododer.databinding.FragmentScreenTodoArchiveDetailBinding
import kanti.tododer.ui.fragments.screens.todo_archive_detail.viewmodel.TodoArchiveDetailViewModel

class TodoArchiveDetailScreenFragment : Fragment() {

	private var _viewBinding: FragmentScreenTodoArchiveDetailBinding? = null
	private val viewBinding: FragmentScreenTodoArchiveDetailBinding get() = _viewBinding!!

	private val viewModel: TodoArchiveDetailViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
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

}