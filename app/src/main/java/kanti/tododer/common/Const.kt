package kanti.tododer.common

import android.content.Context
import kanti.tododer.R

object Const {

	const val DATABASE_NAME = "tododerDatabase"

	const val ROOT_PARENT_ID = "root"

	private lateinit var _NAVIGATION_ARGUMENT_FULL_ID: String
	val NAVIGATION_ARGUMENT_FULL_ID: String
		get() = _NAVIGATION_ARGUMENT_FULL_ID

	fun init(context: Context) = context.apply {
		_NAVIGATION_ARGUMENT_FULL_ID = getString(R.string.navigation_argument_full_id)
	}

	object LogTag {
		const val METHOD = "method"
		const val BUSINESS_LOGIC = "business_logic"
		const val UI_STATE = "ui_state"

		const val ON_CLICK = "on_click"
	}

}