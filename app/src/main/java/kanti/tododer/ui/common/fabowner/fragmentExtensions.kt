package kanti.tododer.ui.common.fabowner

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun Fragment.setActivityFabOnClickListener(listener: View.OnClickListener?) {
	activityFab?.setOnClickListener(listener)
}

val Fragment.activityFab: FloatingActionButton? get() {
	if (activity != null && activity is IFloatingActionButtonOwner) {
		val fabOwner = requireActivity() as IFloatingActionButtonOwner
		return fabOwner.floatingActionButton
	}
	return null
}

fun Fragment.requireActivityFab(): FloatingActionButton {
	val activity = requireActivity()
	if (activity !is IFloatingActionButtonOwner)
		throw IllegalStateException("Activity $activity not implementation IFloatingActionButtonOwner")
	return (activity as IFloatingActionButtonOwner).floatingActionButton
}