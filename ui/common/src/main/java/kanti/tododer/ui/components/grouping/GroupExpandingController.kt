package kanti.tododer.ui.components.grouping

import kanti.tododer.data.model.settings.SettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GroupExpandingController @Inject constructor(
	private val settingsRepository: SettingsRepository
) {

	private val collapseMap = HashMap<String?, Boolean>()

	suspend fun visit(group: String?): Boolean {
		val expand = collapseMap[group]
		if (expand != null)
			return expand
		val expandDefault = settingsRepository.settings.first().groupExpandDefault
		collapseMap[group] = expandDefault
		return expandDefault
	}

	fun setExpand(group: String?, expand: Boolean) {
		collapseMap[group] = expand
	}

	fun clear() {
		collapseMap.clear()
	}
}