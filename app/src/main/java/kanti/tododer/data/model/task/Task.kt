package kanti.tododer.data.model.task

import kanti.tododer.data.model.common.IdOwner
import kanti.tododer.data.model.common.Todo

interface Task : Todo, IdOwner {

	val parentId: String
	val title: String
	val remark: String
	val done: Boolean

}
