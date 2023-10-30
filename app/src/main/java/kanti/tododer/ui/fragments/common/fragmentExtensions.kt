package kanti.tododer.ui.fragments.common

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> Fragment.observe(
	sharedFlow: SharedFlow<T>,
	lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
	action: suspend (T) -> Unit
) {
	viewLifecycleOwner.lifecycleScope.launch {
		repeatOnLifecycle(lifecycleState) {
			sharedFlow.collectLatest(action)
		}
	}
}

fun <T> Fragment.observe(
	sharedFlow: StateFlow<T>,
	lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
	action: suspend (T) -> Unit
) {
	viewLifecycleOwner.lifecycleScope.launch {
		repeatOnLifecycle(lifecycleState) {
			sharedFlow.collectLatest(action)
		}
	}
}