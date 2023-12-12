package kanti.tododer.common

val Any.logTag: String get() = javaClass.simpleName

val Any.hashLogTag: String get() = javaClass.simpleName + hashCode()