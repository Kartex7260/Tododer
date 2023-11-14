package kanti.tododer.ui.viewmodelfeatures

import kanti.tododer.data.model.common.Todo
import kanti.tododer.domain.deletetodowithchildren.DeleteTodoWithProgenyUseCase
import kanti.tododer.domain.todomove.RepositorySet
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