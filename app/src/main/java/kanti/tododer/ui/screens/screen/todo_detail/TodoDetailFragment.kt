package kanti.tododer.ui.screens.screen.todo_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kanti.tododer.R
import kanti.tododer.common.Const
import kanti.tododer.databinding.FragmentTodoDetailBinding
import kanti.tododer.ui.screens.screen.todo_detail.viewmodel.TodoDetailViewModel
import kanti.tododer.ui.screens.screen.todo_detail.viewmodel.uistate.TodoDetailUiState

@AndroidEntryPoint
class TodoDetailFragment : Fragment() {

	private lateinit var view: FragmentTodoDetailBinding
	private val viewModel: TodoDetailViewModel by activityViewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		requireArguments().apply {
			val planFullId = getString(Const.NAVIGATION_ARGUMENT_FULL_ID) as String
			viewModel.pushTodo(planFullId)
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
		viewModel.todoDetailLiveDataLiveData.observe(viewLifecycleOwner) { uiState ->
			showProcess(uiState.process)

			when (uiState.type) {
				is TodoDetailUiState.Type.Success -> {}
				is TodoDetailUiState.Type.EmptyStack -> back()
				is TodoDetailUiState.Type.InvalidFullId -> toastAndBack(R.string.invalid_data, uiState)
				is TodoDetailUiState.Type.NotFound -> toastAndBack(R.string.not_found, uiState)
				else -> toastAndBack(R.string.unexpected_error, uiState)
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

	private fun back() {
		val action = TodoDetailFragmentDirections.actionDetailToList()
		findNavController().navigate(action)
	}

	private fun toastAndBack(@StringRes resId: Int, uiState: TodoDetailUiState) {
		val mess = getString(resId)
		Toast.makeText(
			requireContext(),
			"$mess: ${uiState.type.message}",
			Toast.LENGTH_SHORT
		).show()
		back()
	}

}