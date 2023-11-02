package kanti.tododer.ui.common.toolbarowner

import android.view.MenuInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.MenuProvider
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.appbar.MaterialToolbar
import kanti.tododer.R

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
	lifecycleOwner: LifecycleOwner,
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
				toolbar.findViewById<AppCompatImageButton>(R.id.toolbarButtonMore).also { buttonMore ->
					buttonMore.visibility = View.VISIBLE
					buttonMore.setOnCreateContextMenuListener { menu, _, _ ->
						menuProvider.onCreateMenu(menu, MenuInflater(toolbar.context))
						for (item in menu.iterator()) {
							item.setOnMenuItemClickListener { menuProvider.onMenuItemSelected(it) }
						}
					}
					buttonMore.setOnClickListener {
						showContextMenuForChild(
							it,
							it.x,
							it.y
						)
					}
				}
			}
		}
	}

	override fun onStop(owner: LifecycleOwner) {
		super.onStop(owner)
		toolbar.apply {
			title = defTitle?.let { context.getString(it) }
			navigationIcon = null
			setNavigationOnClickListener {  }

			findViewById<AppCompatImageButton>(R.id.toolbarButtonMore).also { buttonMore ->
				buttonMore.visibility = View.GONE
				buttonMore.setOnCreateContextMenuListener { _, _, _ -> }
				buttonMore.setOnClickListener {  }
			}
		}
	}

}