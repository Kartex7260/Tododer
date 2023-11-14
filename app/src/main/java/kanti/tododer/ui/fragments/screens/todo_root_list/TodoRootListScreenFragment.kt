package kanti.tododer.ui.fragments.screens.todo_root_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
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
import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.isSuccess
import kanti.tododer.ui.fragments.components.todo_list.viewmodel.TodoListViewModel
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.databinding.FragmentScreenTodoRootListBinding
import kanti.tododer.ui.common.fabowner.setActivityFabOnClickListener
import kanti.tododer.ui.common.toolbarowner.setActivityToolbar
import kanti.tododer.ui.fragments.common.observe
import kanti.tododer.ui.fragments.components.todo_list.viewmodel.TodoListUserViewModel

@AndroidEntryPoint
class TodoRootListScreenFragment : Fragment() {

	private lateinit var view: FragmentScreenTodoRootListBinding
	private val viewModel: TodoRootListFeature by viewModels()
	private val todoListViewModel: TodoListUserViewModel by viewModels<TodoListViewModel>()

	private val menuProvider by lazy {
		TodoRootListMenuProvide(
			findNavController(),
			TodoRootListScreenFragmentDirections.actionListToPreferences(),
			TodoRootListScreenFragmentDirections.actionRootToArchive()
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
		view = FragmentScreenTodoRootListBinding.inflate(inflater, container, false)

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
			val `continue` = showMessageFromType(uiState.type)
			if (!`continue`)
				return@observe

			navigateToDetailScreen(uiState.value)
		}

		viewModel.plansLiveData.observe(viewLifecycleOwner) { uiState ->
			processView(uiState.process)
			showData(uiState.value)

			if (!uiState.isSuccess) {
				Toast.makeText(requireContext(), R.string.unexpected_error, Toast.LENGTH_SHORT).show()
			}
		}

		observe(todoListViewModel.elementClick) { todoElement ->
			Log.d(
				this@TodoRootListScreenFragment.hashLogTag,
				"onElementClick.collectLatest { $todoElement }"
			)
			navigateToDetailScreen(todoElement)
		}

		observe(todoListViewModel.planProgressRequest) { progressRequest ->
			Log.d(
				this@TodoRootListScreenFragment.hashLogTag,
				"onPlanProgressRequest.collectLatest { $progressRequest }"
			)
			viewModel.planProgressRequest(progressRequest.plan, progressRequest.callback)
		}

		observe(todoListViewModel.deleteTodo) { deleteRequest ->
			Log.d(
				this@TodoRootListScreenFragment.hashLogTag,
				"onDeleteTodo.collectLatest { $deleteRequest }"
			)
			viewModel.deleteTodo(deleteRequest.todo)
		}

		observe(todoListViewModel.todoItemCreateContextMenu) { createMenuRequest ->
			createMenuRequest.contextMenu.add(
				Menu.NONE,
				1,
				0,
				R.string.to_archive
			).apply {
				setOnMenuItemClickListener {
					viewModel.toArchive(createMenuRequest.todo)
					todoListViewModel.removeTodoView(createMenuRequest.todo)
					true
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

	private fun showData(plans: List<BasePlan>) {
		todoListViewModel.sendTodoList(plans)
	}

	private fun navigateToDetailScreen(todoElement: Todo) {
		val navDirections = TodoRootListScreenFragmentDirections.actionListToDetail(todoElement.fullId)
		findNavController().navigate(navDirections)
	}

	private fun showMessageFromType(type: RepositoryResult.Type): Boolean {
		return when(type) {
			is RepositoryResult.Type.SuccessLocal -> { true }
			is RepositoryResult.Type.AlreadyExists -> {
				Toast.makeText(
					requireContext(),
					"${getString(R.string.not_found)}: ${type.fullId}",
					Toast.LENGTH_SHORT
				).show()
				false
			}
			is RepositoryResult.Type.NotFound -> {
				Toast.makeText(
					requireContext(),
					"${getString(R.string.not_found)}: ${type.message}",
					Toast.LENGTH_SHORT
				).show()
				false
			}
			else -> {
				Toast.makeText(
					requireContext(),
					"${getString(R.string.unexpected_error)}: ${type.message}",
					Toast.LENGTH_SHORT
				).show()
				false
			}
		}
	}

}