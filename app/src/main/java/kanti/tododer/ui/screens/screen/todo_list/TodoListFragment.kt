package kanti.tododer.ui.screens.screen.todo_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kanti.tododer.R
import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.UiState
import kanti.tododer.data.common.isFail
import kanti.tododer.data.common.isSuccess
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.fullId
import kanti.tododer.databinding.FragmentTodoListBinding
import kanti.tododer.ui.view.recycler_adapter.PlanRecyclerAdapter

@AndroidEntryPoint
class TodoListFragment : Fragment() {

	private lateinit var view: FragmentTodoListBinding
	private val viewModel: TodoListViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		view = FragmentTodoListBinding.inflate(inflater, container, false)

		view.recyclerViewRootPlans.layoutManager = LinearLayoutManager(requireContext())

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
		if (plans.isEmpty()) {
			view.recyclerViewRootPlans.adapter = null
			return
		}

		view.recyclerViewRootPlans.adapter = PlanRecyclerAdapter(
			plans = plans,
			onItemClick = ::onItemClick
		)
	}

	private fun onItemClick(plan: Plan) {
		val navDirections = TodoListFragmentDirections.actionListToDetail(plan.fullId)
		view.root.findNavController().navigate(navDirections)
	}

}