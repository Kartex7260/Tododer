package kanti.tododer.ui.screen.todo_list.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.data.model.plan.toPlan
import kanti.tododer.domain.getplanchildren.GetPlanChildren
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.components.todo.TodoData
import kanti.tododer.ui.components.todo.TodosData
import kanti.todoer.data.appdata.AppDataRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TodoListViewModelImpl @Inject constructor(
	appDataRepository: AppDataRepository,
	private val planRepository: PlanRepository,
	private val getPlanChildren: GetPlanChildren,
	@ApplicationContext context: Context
) : ViewModel(), TodoListViewModel {

	override val currentPlan: StateFlow<TodoListUiState> = appDataRepository.currentPlanId
		.map { currentPlanId ->
			var plan = planRepository.getPlanOrDefault(currentPlanId)
			plan = when (plan.type) {
				PlanType.All -> plan.toPlan(title = context.getString(R.string.plan_all))
				PlanType.Default -> plan.toPlan(title = context.getString(R.string.plan_default))
				else -> plan
			}

			val children = getPlanChildren(plan.id)

			TodoListUiState.Success(
				plan = plan,
				children = TodosData(
					todos = children.map { todo ->
						TodoData(
							id = todo.id,
							title = todo.title,
							remark = todo.remark,
							isDone = todo.done
						)
					}
				)
			)
		}
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = TodoListUiState.Empty
		)

	override val todoDeleted: SharedFlow<String>
		get() = MutableSharedFlow()

	override fun changeDone(todoId: Long, isDone: Boolean) {
	}

	override fun deleteTodo(todoId: Long) {
	}

	override fun undoDelete() {
	}
}