package kanti.tododer.domain.archiving.unarchive

import kanti.tododer.data.model.plan.BasePlan
import javax.inject.Inject

class UnarchivePlanUseCase @Inject constructor(

) {

	suspend operator fun invoke(task: BasePlan) {
	}

}