package kanti.tododer.domain

import kanti.tododer.data.model.common.ParentOwner
import kanti.tododer.data.model.common.Todo
import kanti.tododer.domain.todomove.RepositorySet
import javax.inject.Inject

class GetForerunnersUseCase @Inject constructor(
	private val getParentUseCase: GetParentUseCase
) {

	suspend operator fun invoke(
		repositorySet: RepositorySet,
		parentOwner: ParentOwner
	): List<Todo> {
		val forerunners = mutableListOf<Todo>()
		getForerunner(forerunners, repositorySet, parentOwner)
		return forerunners
	}

	private suspend fun getForerunner(
		mutableList: MutableList<Todo>,
		repositorySet: RepositorySet,
		parentOwner: ParentOwner
	) {
		val parent = getParentUseCase(repositorySet, parentOwner) ?: return
		mutableList.add(parent)
		getForerunner(mutableList, repositorySet, parent)
	}

}