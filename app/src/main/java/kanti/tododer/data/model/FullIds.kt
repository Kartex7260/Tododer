package kanti.tododer.data.model

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task

object FullIds {

	private const val planPrefix = "plan"
	private const val taskPrefix = "task"
	private const val separator = '-'

	fun from(plan: Plan): String = from(planPrefix, plan.id)

	fun from(task: Task): String = from(taskPrefix, task.id)

	private fun from(fullId: FullId): String {
		return when (fullId.type) {
			Type.Task -> from(taskPrefix, fullId.id)
			Type.Plan -> from(planPrefix, fullId.id)
		}
	}

	private fun from(prefix: String, id: Int): String {
		return "$prefix$separator$id"
	}

	fun parseFullId(fullId: String): FullId? {
		val split = split(fullId)
		val type = getType(split)
		val id = getId(split)
		if (type == null || id == null)
			return null
		return FullId(type, id)
	}

	private fun getId(split: List<String>): Int? {
		if (split.size < 2)
			return null
		return split[1].toIntOrNull()
	}

	private fun getType(split: List<String>): Type? {
		if (split.isEmpty())
			return null
		return when (split[0]) {
			planPrefix -> Type.Plan
			taskPrefix -> Type.Task
			else -> null
		}
	}

	private fun split(fullId: String): List<String> {
		return fullId.split(separator)
	}

	sealed class Type {
		data object Task : Type()
		data object Plan : Type()
	}

}