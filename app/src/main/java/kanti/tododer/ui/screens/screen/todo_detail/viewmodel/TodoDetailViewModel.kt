package kanti.tododer.ui.screens.screen.todo_detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIds
import kanti.tododer.domain.plan.planwithchildren.GetPlanWithChildrenUseCase
import kanti.tododer.domain.task.taskwithchildren.GetTaskWithChildrenUseCase
import kotlinx.coroutines.launch
import java.util.EmptyStackException
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(
	private val getTaskWithChildren: GetTaskWithChildrenUseCase,
	private val getPlanWithChildren: GetPlanWithChildrenUseCase
) : ViewModel() {



}