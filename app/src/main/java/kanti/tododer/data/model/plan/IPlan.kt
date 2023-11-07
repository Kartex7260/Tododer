package kanti.tododer.data.model.plan

import kanti.tododer.data.model.common.Todo

interface IPlan : Todo {

	val parentId: String
	val title: String
	val remark: String

}