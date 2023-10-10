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

}