package kanti.tododer

import android.app.Application
import android.os.Build
import androidx.preference.PreferenceManager
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import dagger.hilt.android.HiltAndroidApp
import kanti.tododer.common.Const
import kanti.tododer.ui.fragments.preferences.main.darkmode.DarkMode
import kanti.tododer.ui.fragments.preferences.main.darkmode.changeDarkMode24

@HiltAndroidApp
class TododerApplication : Application() {

	override fun onCreate() {
		super.onCreate()
		kanti.tododer.common.Const.init(this)
		DynamicColors.applyToActivitiesIfAvailable(
			this,
			DynamicColorsOptions.Builder()
				.build()
		)

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
			val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
			val darkModeString = sharedPreferences.getString(
				getString(R.string.preferences_main_theme_value),
				getString(R.string.preferences_main_theme_as_system_value)
			) as String

			val darkMode = DarkMode.parse(this, darkModeString)
			changeDarkMode24(darkMode)
		}
	}

}