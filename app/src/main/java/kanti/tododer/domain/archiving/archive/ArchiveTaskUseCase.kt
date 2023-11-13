package kanti.tododer.domain.archiving.archive

import kanti.tododer.data.model.task.BaseTask
import javax.inject.Inject

class ArchiveTaskUseCase @Inject constructor(

) {

	suspend operator fun invoke(task: BaseTask) {
	}

}