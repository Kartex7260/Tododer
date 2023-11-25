package kanti.tododer.data.model.plan

import kanti.tododer.data.model.common.Todo

interface Plan : Todo {

	val title: String
	val remark: String

}