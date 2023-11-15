package kanti.tododer.common.features

import kanti.tododer.data.model.common.Todo
import kanti.tododer.domain.archiving.ArchiveTodoUseCase
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch

interface ArchiveTodoFeature : CoroutineScopeFeature {

	val archiveTodoUseCase: ArchiveTodoUseCase

	fun archiveTodo(todo: Todo) {
		coroutineScope.launch(NonCancellable) {
			archiveTodoUseCase(todo)
		}
	}

}