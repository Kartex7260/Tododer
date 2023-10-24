package kanti.tododer.ui.screens.todo_root_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kanti.lifecyclelogger.LifecycleLogger
import kanti.tododer.R
import kanti.tododer.common.hashLogTag
import kanti.tododer.data.common.isSuccess
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.databinding.FragmentTodoRootBinding
import kanti.tododer.ui.fragments.components.todo_list.viewmodel.TodoListViewModel
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.fullId
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoRootListScreenFragment : Fragment() {

	private lateinit var view: FragmentTodoRootBinding
	private val viewModel: TodoRootListViewModel by viewModels()
	private val todoListViewModel: TodoListViewModel by viewModels()

	private lateinit var lifecycleLogger: LifecycleLogger

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		lifecycleLogger = LifecycleLogger(this)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		view = FragmentTodoRootBinding.inflate(inflater, container, false)

		return view.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.plansLiveData.observe(viewLifecycleOwner) { uiState ->
			processView(uiState.process)
			showData(uiState.value)

			if (!uiState.isSuccess) {
				Toast.makeText(requireContext(), R.string.unexpected_error, Toast.LENGTH_SHORT).show()
			}
		}

		viewLifecycleOwner.lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				todoListViewModel.onElementClickSharedFlow.collectLatest { todoElement ->
					Log.d(
						this@TodoRootListScreenFragment.hashLogTag,
						"onElementClickSharedFlow.collectLatest { $todoElement }"
					)
					navigateToDetailScreen(todoElement)
				}
			}
		}
	}

	override fun onResume() {
		super.onResume()
		viewModel.getRootPlans()
	}

	private fun processView(process: Boolean) {
		if (process) {
			view.progressBarTodoList.visibility = View.VISIBLE
		} else {
			view.progressBarTodoList.visibility = View.INVISIBLE
		}
	}

	private fun showData(plans: List<Plan>) {
		todoListViewModel.sendTodoList(plans)
	}

	private fun navigateToDetailScreen(todoElement: Todo) {
		val navDirections = TodoRootListScreenFragmentDirections.actionListToDetail(todoElement.fullId)
		view.root.findNavController().navigate(navDirections)
	}

}