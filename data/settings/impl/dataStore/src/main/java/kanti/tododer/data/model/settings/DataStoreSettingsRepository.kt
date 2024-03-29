package kanti.tododer.data.model.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
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
	private val selectionStyleKey = stringPreferencesKey("selectionStyles")
	private val selectionStylesSeparator = ","
	private val groupExpandDefaultKey = booleanPreferencesKey("groupExpandDefault")

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

			val selectionStyle = preferences[selectionStyleKey] ?: run { "" }.also { style ->
				if (style.isBlank()) {
					resetMultiSelectionStyle()
					returnNull = true
				}
			}

			val groupExpandDefault = preferences[groupExpandDefaultKey] ?: run {
				resetGroupExpandDefault()
				returnNull = true
				true
			}

			if (returnNull)
				return@map null
			SettingsData(
				appTheme = AppTheme.valueOf(appThemeStg),
				colorStyleId = colorStyleId,
				multiSelectionStyleFlags = selectionStyle
					.split(selectionStylesSeparator)
					.map { SelectionStyle.valueOf(it) }.toSet(),
				groupExpandDefault = groupExpandDefault
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

	override suspend fun setMultiSelectionStyles(selection: Set<SelectionStyle>) {
		context.dataStore.edit { preferences ->
			preferences[selectionStyleKey] = selection
				.joinToString(separator = selectionStylesSeparator) { it.name }
		}
	}

	override suspend fun setGroupExpandDefault(expandDefault: Boolean) {
		context.dataStore.edit { preferences ->
			preferences[groupExpandDefaultKey] = expandDefault
		}
	}
}