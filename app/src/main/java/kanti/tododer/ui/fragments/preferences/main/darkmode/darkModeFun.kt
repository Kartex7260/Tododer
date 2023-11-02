package kanti.tododer.ui.fragments.preferences.main.darkmode

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate

fun Context.changeDarkMode(darkMode: DarkMode) {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
		changeDarkMode31(darkMode)
	} else {
		changeDarkMode24(darkMode)
	}
}

@RequiresApi(Build.VERSION_CODES.S)
private fun Context.changeDarkMode31(darkMode: DarkMode) {
	val uiManager = getSystemService(UiModeManager::class.java) as UiModeManager
	when (darkMode) {
		DarkMode.LIGHT -> {
			uiManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
		}
		DarkMode.DARK -> {
			uiManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)
		}
		DarkMode.AS_SYSTEM -> {
			uiManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_CUSTOM)
		}
	}
}

fun changeDarkMode24(darkMode: DarkMode) {
	when (darkMode) {
		DarkMode.LIGHT -> {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
		}
		DarkMode.DARK -> {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
		}
		DarkMode.AS_SYSTEM -> {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
		}
	}
}