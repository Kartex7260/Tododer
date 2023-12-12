package kanti.tododer.data.model.common

fun <T> localUnexpectedByRowId(todo: Todo, rowId: Long): Result<T> {
	return Result.failure(
		IllegalStateException("Unexpected error: not found by rowId: Todo=$todo, rowId=$rowId")
	)
}