package kanti.tododer.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kanti.todoer.data.appdata.AppDataLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val dataStoreName = "appData"
private val Context.dataStore by preferencesDataStore(name = dataStoreName)

class AppDataStoreDataSource @Inject constructor(
	@ApplicationContext private val appContext: Context
) : AppDataLocalDataSource {

	private val _currentPlanIdKey = "currentPlanId"

	private val currentPlanIdKey = longPreferencesKey(name = _currentPlanIdKey)

	override val currentPlanId: Flow<Long?>
		get() = appContext.dataStore.data.map {  preferences ->
			preferences[currentPlanIdKey]
		}

	override suspend fun setCurrentPlan(planId: Long?) {
		appContext.dataStore.edit { preferences ->
			if (planId != null) {
				preferences[currentPlanIdKey] = planId
			} else {
				preferences.remove(currentPlanIdKey)
			}
		}
	}
}