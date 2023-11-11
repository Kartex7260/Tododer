package kanti.tododer.ui.fragments.screens.todo_archive_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kanti.tododer.databinding.FragmentScreenTodoArchiveListBinding

class TodoArchiveListScreenFragment : Fragment() {

	private var _viewBinding: FragmentScreenTodoArchiveListBinding? = null
	private val viewBinding: FragmentScreenTodoArchiveListBinding get() = _viewBinding!!

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

}