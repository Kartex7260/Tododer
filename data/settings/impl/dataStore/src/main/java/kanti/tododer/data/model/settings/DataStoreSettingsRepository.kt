package kanti.tododer.data.model.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val dataStoreName = "app_settings"
private val Context.dataStore by preferencesDataStore(name = dataStoreName)

class DataStoreSettingsRepository @Inject constructor(
	@ApplicationContext val context: Context
) : SettingsRepository {

	private val appThemeKey = stringPreferencesKey("appTheme")

	override val settings: Flow<SettingsData> = context.dataStore.data
		.map { preferences ->
			var returnNull = false
			val appThemeStg = preferences[appThemeKey] ?: run {
				setTheme(AppTheme.AS_SYSTEM)
				returnNull = true
				""
			}

			if (returnNull)
				return@map null
			SettingsData(
				appTheme = AppTheme.valueOf(appThemeStg)
			)
		}
		.filterNotNull()

	override suspend fun setTheme(theme: AppTheme) {
		context.dataStore.edit { preferences ->
			preferences[appThemeKey] = theme.name
		}
	}
}