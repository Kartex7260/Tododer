package kanti.tododer.services.chanceundo

interface ChanceUndo<T> {

	fun register(value: T)

	fun unregister()

	fun undo(): T?
}