package kanti.tododer.data.model.common

fun Todo.checkType(typeExpected: Todo.Type) {
	check(typeExpected == type) {
		"Try get $typeExpected, but todo is $type"
	}
}