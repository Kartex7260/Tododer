package kanti.tododer.ui.common.fabowner

import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FabOwnerLifecycleObserver(
	lifecycleOwner: LifecycleOwner,
	private val fab: FloatingActionButton,
	private val clickListener: View.OnClickListener? = null
) : DefaultLifecycleObserver {

	init {
		lifecycleOwner.lifecycle.addObserver(this)
	}

	override fun onStart(owner: LifecycleOwner) {
		fab.apply {
			visibility = View.VISIBLE
			setOnClickListener { clickListener?.onClick(it) }
		}
	}

	override fun onStop(owner: LifecycleOwner) {
		fab.apply {
			visibility = View.GONE
			setOnClickListener {  }
		}
	}

}