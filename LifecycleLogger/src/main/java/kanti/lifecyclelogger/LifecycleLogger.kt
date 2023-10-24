package kanti.lifecyclelogger

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

class LifecycleLogger: DefaultLifecycleObserver {

	private val name: String

	constructor(lifecycleOwner: LifecycleOwner, name: String? = null) {
		lifecycleOwner.lifecycle.addObserver(this)
		if (name == null) {
			this.name = lifecycleOwner.javaClass.simpleName + lifecycleOwner.hashCode()
		} else {
			this.name = name
		}
	}

	constructor(lifecycle: Lifecycle, name: String) {
		lifecycle.addObserver(this)
		this.name = name
	}

	override fun onCreate(owner: LifecycleOwner) {
		super.onCreate(owner)
		Log.d(name, "onCreate()")
	}

	override fun onStart(owner: LifecycleOwner) {
		super.onStart(owner)
		Log.d(name, "onStart()")
	}

	override fun onResume(owner: LifecycleOwner) {
		super.onResume(owner)
		Log.d(name, "onResume()")
	}

	override fun onPause(owner: LifecycleOwner) {
		super.onPause(owner)
		Log.d(name, "onPause()")
	}

	override fun onStop(owner: LifecycleOwner) {
		super.onStop(owner)
		Log.d(name, "onPause()")
	}

	override fun onDestroy(owner: LifecycleOwner) {
		super.onDestroy(owner)
		Log.d(name, "onDestroy()")
	}

}