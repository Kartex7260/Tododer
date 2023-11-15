package kanti.tododer.common.features

import kanti.tododer.data.model.common.Todo
import kanti.tododer.domain.archiving.UnarchiveTodoUseCase
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch

interface UnarchiveTodoFeature : CoroutineScopeFeature {

	val unarchiveTodoUseCase: UnarchiveTodoUseCase

	fun unarchiveTodo(todo: Todo) {
		coroutineScope.launch(NonCancellable) {
			unarchiveTodoUseCase(todo)
		}
	}

}