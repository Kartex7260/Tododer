package kanti.tododer.data.model.task

import kanti.tododer.data.model.common.Todo

interface Task : Todo {

	val title: String
	val remark: String
	val done: Boolean

}
