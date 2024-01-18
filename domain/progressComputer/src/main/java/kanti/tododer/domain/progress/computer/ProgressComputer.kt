package kanti.tododer.domain.progress.computer

class ProgressComputer {

	private val todos: MutableMap<Long, Boolean> = HashMap()

	val progress: Float get() = todos.count { it.value } / todos.size.toFloat()

	fun add(id: Long, isDone: Boolean) {
		todos[id] = isDone
	}

	fun remove(id: Long) {
		todos.remove(id)
	}
}