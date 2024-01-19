package kanti.tododer.domain.progress.computer

class ProgressComputer {

	private val todos: MutableMap<Long, Boolean> = HashMap()

	val progress: Float get() {
		val result = todos.count { it.value } / todos.size.toFloat()
		if (result.isNaN())
			return 0f
		return result
	}

	fun add(id: Long, isDone: Boolean) {
		todos[id] = isDone
	}

	fun remove(id: Long) {
		todos.remove(id)
	}
}