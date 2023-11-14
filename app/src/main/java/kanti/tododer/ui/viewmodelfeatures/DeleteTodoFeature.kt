package kanti.tododer.ui.viewmodelfeatures

import kanti.tododer.data.model.common.Todo
import kanti.tododer.domain.removewithchildren.RemoveTodoWithProgenyUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch

interface DeleteTodoFeature : CoroutineScopeFeature {

	val removeTodoWithProgenyUseCase: RemoveTodoWithProgenyUseCase

	fun deleteTodo(todo: Todo) {
		coroutineScope.launch(NonCancellable) {
			removeTodoWithProgenyUseCase(todo)
		}
	}

}