package kanti.tododer.data.model

data class ParentId(
	val id: Int,
	val type: ParentType
) {
	override fun toString(): String {
		return "$type$SEPARATOR$id"
	}

	companion object {

		private const val SEPARATOR = "-"

		fun from(line: String): ParentId {
			val splitParts = line.split(SEPARATOR)
			if (splitParts.size != 2)
				throw IllegalArgumentException("Invalid line = $line")
			return ParentId(
				id = splitParts[1].toInt(),
				type = ParentType.valueOf(splitParts[0])
			)
		}
	}
}

enum class ParentType {
	Plan,
	Task
}
