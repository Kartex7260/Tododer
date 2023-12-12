package kanti.tododer.ui.fragments.screens.todo_root_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kanti.lifecyclelogger.LifecycleLogger
import kanti.tododer.R
import kanti.tododer.common.hashLogTag
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.databinding.FragmentTodoRootBinding
import kanti.tododer.ui.fragments.components.todo_list.viewmodel.TodoListViewModel
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.result.ResultUiState
import kanti.tododer.ui.common.fabowner.setActivityFabOnClickListener
import kanti.tododer.ui.common.toolbarowner.setActivityToolbar
import kanti.tododer.ui.fragments.common.observe

@AndroidEntryPoint
class TodoRootListScreenFragment : Fragment() {

	private lateinit var view: FragmentTodoRootBinding
	private val viewModel: TodoRootListViewModel by viewModels()
	private val todoListViewModel: TodoListViewModel by viewModels()

	private val menuProvider by lazy {
		TodoRootListMenuProvide(
			findNavController(),
			TodoRootListScreenFragmentDirections.actionListToPreferences()
		)
	}

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

		setActivityToolbar(
			title = R.string.app_name,
			lifecycleOwner = viewLifecycleOwner,
			menuProvider = menuProvider
		)

		setActivityFabOnClickListener {
			viewModel.createNewPlan()
		}

		observe(viewModel.newPlanCreated) { uiState ->
			when(uiState) {
				is ResultUiState.Success -> {
					navigateToDetailScreen(uiState.value)
				}
				is ResultUiState.Process -> {}
				is ResultUiState.Fail -> {
					Toast.makeText(
						requireContext(),
						"${getString(R.string.unexpected_error)}: ${uiState.message}",
						Toast.LENGTH_SHORT
					).show()
				}
			}
		}

		viewModel.plansLiveData.observe(viewLifecycleOwner) { uiState ->
			processView(false)
			when (uiState) {
				is ResultUiState.Process -> {
					processView(true)
				}
				is ResultUiState.Success -> {
					showData(uiState.value)
				}
				is ResultUiState.Fail -> {
					val stg = getString(R.string.unexpected_error)
					Toast.makeText(
						requireContext(),
						"$stg: ${uiState.message}",
						Toast.LENGTH_SHORT
					).show()
				}
			}
		}

		observe(todoListViewModel.onElementClick) { todoElement ->
			Log.d(
				this@TodoRootListScreenFragment.hashLogTag,
				"onElementClick.collectLatest { $todoElement }"
			)
			navigateToDetailScreen(todoElement)
		}

		observe(todoListViewModel.onPlanProgressRequest) { progressRequest ->
			Log.d(
				this@TodoRootListScreenFragment.hashLogTag,
				"onPlanProgressRequest.collectLatest { $progressRequest }"
			)
			viewModel.planProgressRequest(progressRequest.plan, progressRequest.callback)
		}

		observe(todoListViewModel.onDeleteTodo) { deleteRequest ->
			Log.d(
				this@TodoRootListScreenFragment.hashLogTag,
				"onDeleteTodo.collectLatest { $deleteRequest }"
			)
			viewModel.deleteTodo(deleteRequest.todo)
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

	private fun showData(plans: List<kanti.tododer.data.model.plan.Plan>) {
		todoListViewModel.sendTodoList(plans)
	}

	private fun navigateToDetailScreen(todoElement: kanti.tododer.data.model.common.Todo) {
		val navDirections = TodoRootListScreenFragmentDirections.actionListToDetail(todoElement.fullId)
		findNavController().navigate(navDirections)
	}

}