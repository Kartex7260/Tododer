package kanti.tododer.domain

import kanti.tododer.data.model.common.FullIds
import kanti.tododer.data.model.common.ParentOwner
import kanti.tododer.data.model.common.Todo
import kanti.tododer.domain.todomove.RepositorySet
import javax.inject.Inject

class GetParentUseCase @Inject constructor() {

	suspend operator fun invoke(
		repositorySet: RepositorySet,
		parentOwner: ParentOwner
	): Todo? {
		val parentFullId = FullIds.parseFullId(parentOwner.parentId) ?: return null
		return when (parentFullId.type) {
			Todo.Type.PLAN -> repositorySet.planRepository.getTodo(parentFullId.id).value
			Todo.Type.TASK -> repositorySet.taskRepository.getTodo(parentFullId.id).value
		}
	}

}