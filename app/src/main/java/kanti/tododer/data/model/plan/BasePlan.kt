package kanti.tododer.data.model.plan

import kanti.tododer.data.model.common.Todo

interface BasePlan : Todo {

	val parentId: String
	val title: String
	val remark: String

}

val Todo.asBasePlan: BasePlan get() {
	return this as BasePlan
}