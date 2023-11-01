package kanti.tododer.ui.common.toolbarowner

import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar

val Fragment.activityToolbar: MaterialToolbar? get() {
	if (activity == null || activity !is IToolbarOwner)
		return null
	return (activity as IToolbarOwner).toolbar
}

fun Fragment.requireActivityToolbar(): MaterialToolbar {
	val activity = requireActivity()
	if (activity !is IToolbarOwner)
		throw IllegalStateException("Activity not implementation IToolbarOwner")
	return (activity as IToolbarOwner).toolbar
}