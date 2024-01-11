package kanti.tododer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.domain.datainitializer.InitializeData
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val initializeData: InitializeData
) : ViewModel() {

	fun init() {
		viewModelScope.launch {
			initializeData()
		}
	}
}