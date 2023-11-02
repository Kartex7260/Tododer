package kanti.tododer.ui.common.toolbarowner

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.appbar.MaterialToolbar
import java.security.PrivateKey

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

fun Fragment.setActivityToolbar(
	@StringRes title: Int,
	@StringRes defTitle: Int? = null,
	@DrawableRes navIcon: Int? = null,
	lifecycleOwner: LifecycleOwner,
	menuProvider: MenuProvider? = null,
	click: ((View) -> Unit)? = null
) {
	activityToolbar?.also { toolbar ->
		ToolbarSetup(
			toolbar,
			title,
			defTitle,
			navIcon,
			lifecycleOwner,
			menuProvider,
			click
		)
	}
}

class ToolbarSetup(
	private val toolbar: MaterialToolbar,
	@StringRes private val title: Int,
	@StringRes private val defTitle: Int? = null,
	@DrawableRes private val navIcon: Int? = null,
	private val lifecycleOwner: LifecycleOwner,
	private val menuProvider: MenuProvider? = null,
	private val click: ((View) -> Unit)? = null
) : DefaultLifecycleObserver {

	init {
		lifecycleOwner.lifecycle.addObserver(this)
	}

	override fun onStart(owner: LifecycleOwner) {
		super.onStart(owner)
		toolbar.apply {
			title = context.getString(this@ToolbarSetup.title)
			navIcon?.also {
				navigationIcon = AppCompatResources.getDrawable(context, it)
			}
			setNavigationOnClickListener(click)

			menuProvider?.also {
				addMenuProvider(it, lifecycleOwner)
			}
		}
	}

	override fun onStop(owner: LifecycleOwner) {
		super.onStop(owner)
		toolbar.apply {
			title = defTitle?.let { context.getString(it) }
			navigationIcon = null
			setNavigationOnClickListener {  }
		}
	}

}