package kanti.tododer.services.chanceundo

import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import javax.inject.Inject

class ChanceUndoImpl<T> @Inject constructor(
	@StandardLog private val logger: Logger
) : ChanceUndo<T>{

	private val logTag = "ChanceUndoImpl<T>"

	private var value: T? = null

	override fun register(value: T) {
		logger.d(logTag, "register(T = $value)")
		this.value = value
	}

	override fun unregister(): T? {
		val value = this.value
		logger.d(logTag, "unregister(): return $value")
		this.value = null
		return value
	}
}