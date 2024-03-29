package kanti.tododer.data.model

data class FullId(
	val id: Long = 0,
	val type: FullIdType = FullIdType.Plan
) {
	override fun toString(): String {
		return "$type$SEPARATOR$id"
	}

	companion object {

		private const val SEPARATOR = ":"

		fun from(line: String): FullId {
			val splitParts = line.split(SEPARATOR)
			if (splitParts.size != 2)
				throw IllegalArgumentException("Invalid line = $line")
			return FullId(
				id = splitParts[1].toLong(),
				type = FullIdType.valueOf(splitParts[0])
			)
		}
	}
}

enum class FullIdType {
	Plan,
	Todo
}
