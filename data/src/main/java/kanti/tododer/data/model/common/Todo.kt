package kanti.tododer.data.model.common

import kanti.tododer.data.model.common.fullid.FullIds

interface Todo : IdOwner, ParentOwner {

	val type: Type

	val fullId: String get() {
		return FullIds.from(this)
	}

	enum class Type {
		PLAN,
		TASK
	}

}
