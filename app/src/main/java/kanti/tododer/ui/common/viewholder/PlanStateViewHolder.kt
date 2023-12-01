package kanti.tododer.ui.common.viewholder

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import kanti.fillingprogressview.FillingProgressView
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.result.GetRepositoryResult
import kanti.tododer.data.model.common.result.asSuccess

class PlanStateViewHolder(
	todo: Todo,
	private val layoutInflater: LayoutInflater,
	root: ViewGroup? = RootDefault,
	attachToRoot: Boolean = AttachToRootDefault
) : TodoViewHolder(todo, layoutInflater, NonResource, root, attachToRoot) {

	override val type: Todo.Type = Todo.Type.PLAN

	override fun createView(): View {
		return FillingProgressView(layoutInflater.context).apply {
			setPadding(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP,
				15f,
				layoutInflater.context.resources.displayMetrics
			).toInt())
		}
	}

	override fun onBindData(view: View, todo: Todo) {
		val progressView = view as FillingProgressView

		event(EVENT_PROGRESS_REQUEST, todo) { progress ->
			if (progress == null || progress !is GetRepositoryResult<*>)
				return@event
			val sucProgress = progress.asSuccess ?: return@event
			progressView.progress = sucProgress.value as Float
		}
	}

	companion object {

		const val EVENT_PROGRESS_REQUEST = PlanViewHolder.EVENT_PROGRESS_REQUEST

	}

}