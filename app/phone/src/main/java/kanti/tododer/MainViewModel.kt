package kanti.tododer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.model.plan.PlanRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val planRepository: PlanRepository
) : ViewModel() {

	fun init() {
		viewModelScope.launch {
			planRepository.init()
		}
	}
}