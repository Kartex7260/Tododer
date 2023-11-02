package kanti.tododer.ui.fragments.preferences.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import kanti.tododer.R
import kanti.tododer.ui.common.toolbarowner.requireActivityToolbar
import kanti.tododer.ui.fragments.preferences.main.darkmode.DarkMode
import kanti.tododer.ui.fragments.preferences.main.darkmode.changeDarkMode

class MainPreferences : PreferenceFragmentCompat() {

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.preferences_main, rootKey)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		requireActivityToolbar().apply {
			title = getString(R.string.preferences)
			navigationIcon = AppCompatResources.getDrawable(
				requireContext(),
				R.drawable.baseline_arrow_back_24
			)
			setNavigationOnClickListener {
				this@MainPreferences.findNavController().popBackStack()
			}
		}

		findPreference<ListPreference>(getString(R.string.preferences_main_theme_value))?.apply {
			setOnPreferenceChangeListener { _, newValue ->
				when(newValue) {
					getString(R.string.preferences_main_theme_light_value) -> {
						requireContext().changeDarkMode(DarkMode.LIGHT)
						true
					}
					getString(R.string.preferences_main_theme_dark_value) -> {
						requireContext().changeDarkMode(DarkMode.DARK)
						true
					}
					getString(R.string.preferences_main_theme_as_system_value) -> {
						requireContext().changeDarkMode(DarkMode.AS_SYSTEM)
						true
					}
					else -> false
				}
			}
		}
	}

}