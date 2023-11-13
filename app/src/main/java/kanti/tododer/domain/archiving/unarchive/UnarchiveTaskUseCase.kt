package kanti.tododer.domain.archiving.unarchive

import kanti.tododer.data.model.task.BaseTask
import javax.inject.Inject

class UnarchiveTaskUseCase @Inject constructor(

) {

	suspend operator fun invoke(task: BaseTask) {
	}

}
