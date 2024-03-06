package kanti.tododer.ui.components.grouping

import javax.inject.Inject

class GroupExpandingController @Inject constructor() {

	private val initialExpand = true
	private val collapseMap = HashMap<String?, Boolean>()

	fun visit(group: String?): Boolean {
		val expand = collapseMap[group]
		if (expand != null)
			return expand
		collapseMap[group] = initialExpand
		return initialExpand
	}

	fun setExpand(group: String?, expand: Boolean) {
		collapseMap[group] = expand
	}

	fun clear() {
		collapseMap.clear()
	}
}