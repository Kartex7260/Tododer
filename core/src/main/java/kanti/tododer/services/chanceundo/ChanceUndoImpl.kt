package kanti.tododer.services.chanceundo

import javax.inject.Inject

class ChanceUndoImpl<T> @Inject constructor() : ChanceUndo<T>{

	private var value: T? = null

	override fun register(value: T) {
		this.value = value
	}

	override fun unregister(): T? {
		val value = this.value
		this.value = null
		return value
	}
}