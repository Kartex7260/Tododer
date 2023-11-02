package kanti.tododer.ui.fragments.preferences.main.darkmode

import android.content.Context
import kanti.tododer.R
import java.lang.IllegalArgumentException

enum class DarkMode {

	LIGHT,
	DARK,
	AS_SYSTEM;

	companion object {

		fun parse(context: Context, value: String): DarkMode {
			return when (value) {
				context.getString(R.string.preferences_main_theme_as_system_value) -> AS_SYSTEM
				context.getString(R.string.preferences_main_theme_light_value) -> LIGHT
				context.getString(R.string.preferences_main_theme_dark_value) -> DARK
				else -> throw IllegalArgumentException("Invalid DarkMode string")
			}
		}

	}

}