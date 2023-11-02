package kanti.tododer.ui.common.viewholder

import android.view.View

interface TodoStateViewOwner<StateView> where StateView : View {

	val stateView: StateView

}