package kanti.tododer.ui.common.fabowner

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun Fragment.setActivityFabOnClickListener(listener: View.OnClickListener?) {
	activityFab?.setOnClickListener(listener)
}

val Fragment.activityFab: FloatingActionButton? get() {
	if (activity != null && activity is FloatingActionButtonOwner) {
		val fabOwner = requireActivity() as FloatingActionButtonOwner
		return fabOwner.floatingActionButton
	}
	return null
}

fun Fragment.requireActivityFab(): FloatingActionButton {
	val activity = requireActivity()
	if (activity !is FloatingActionButtonOwner)
		throw IllegalStateException("Activity $activity not implementation IFloatingActionButtonOwner")
	return (activity as FloatingActionButtonOwner).floatingActionButton
}