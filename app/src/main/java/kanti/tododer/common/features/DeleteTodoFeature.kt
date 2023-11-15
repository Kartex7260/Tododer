package kanti.tododer.common.features

import kanti.tododer.data.model.common.Todo
import kanti.tododer.domain.deletetodowithchildren.DeleteTodoWithProgenyUseCase
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch

interface DeleteTodoFeature : CoroutineScopeFeature, RepositorySetFeature {

	val deleteTodoWithProgenyUseCase: DeleteTodoWithProgenyUseCase

	fun deleteTodo(todo: Todo) {
		coroutineScope.launch(NonCancellable) {
			deleteTodoWithProgenyUseCase(repositorySet, todo)
		}
	}

}