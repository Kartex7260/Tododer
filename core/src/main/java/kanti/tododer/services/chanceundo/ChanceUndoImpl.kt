package kanti.tododer.services.chanceundo

import javax.inject.Inject

class ChanceUndoImpl<T> @Inject constructor() : ChanceUndo<T>{

	private var value: T? = null

	override fun register(value: T) {
		this.value = value
	}

	override fun unregister() {
		value = null
	}

	override fun undo(): T? {
		val value = value
		unregister()
		return value
	}
}