package kanti.tododer.data.model.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val dataStoreName = "app_settings"
private val Context.dataStore by preferencesDataStore(name = dataStoreName)

class DataStoreSettingsRepository @Inject constructor(
	@ApplicationContext val context: Context
) : SettingsRepository {

	private val appThemeKey = stringPreferencesKey("appTheme")
	private val colorStyleKey = intPreferencesKey("colorStyle")
	private val selectionStyleKey = intPreferencesKey("selectionStyle")

	override val settings: Flow<SettingsData> = context.dataStore.data
		.map { preferences ->
			var returnNull = false
			val appThemeStg = preferences[appThemeKey] ?: run {
				setTheme(AppTheme.AS_SYSTEM)
				returnNull = true
				""
			}

			val colorStyleId = preferences[colorStyleKey] ?: run {
				resetColorStyle()
				returnNull = true
				0
			}

			val selectionStyle = preferences[selectionStyleKey] ?: run { 0 }.also { style ->
				if (style == 0) {
					resetMultiSelectionStyle()
					returnNull = true
				}
			}

			if (returnNull)
				return@map null
			SettingsData(
				appTheme = AppTheme.valueOf(appThemeStg),
				colorStyleId = colorStyleId,
				multiSelectionStyleFlags = selectionStyle
			)
		}
		.filterNotNull()
		.flowOn(Dispatchers.Default)

	override suspend fun setTheme(theme: AppTheme) {
		context.dataStore.edit { preferences ->
			preferences[appThemeKey] = theme.name
		}
	}

	override suspend fun setColorStyle(colorStyleId: Int) {
		context.dataStore.edit { preferences ->
			preferences[colorStyleKey] = colorStyleId
		}
	}

	override suspend fun setMultiSelectionStyleFlags(flags: Int) {
		context.dataStore.edit { preferences ->
			preferences[selectionStyleKey] = flags
		}
	}
}