package kanti.tododer.ui.fragments.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kanti.tododer.R
import kanti.tododer.data.model.common.Todo

class TodoSelectorDialogFragment : DialogFragment() {

	fun interface TodoSelectListener {
		fun select(type: Todo.Type)
	}

	var todoSelectListener: TodoSelectListener? = null
		private set

	fun setTodoSelectListener(listener: TodoSelectListener? = null) {
		todoSelectListener = listener
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return activity?.run {
			AlertDialog.Builder(this).apply {
				setItems(R.array.todo_types) { dialogInterface, which ->
					when (which) {
						0 -> todoSelectListener?.select(Todo.Type.PLAN)
						1 -> todoSelectListener?.select(Todo.Type.TASK)
					}
					dialogInterface.cancel()
				}
				setNegativeButton(R.string.todo_selector_dialog_negative_button) { dialogInterface, _ ->
					dialogInterface.cancel()
				}
			}.create()
		} ?: throw IllegalStateException("Fragment not attached to activity")
	}

}