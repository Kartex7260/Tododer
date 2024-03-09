package kanti.tododer.data.model.settings

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataStoreSettingsRepositoryTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val repository = DataStoreSettingsRepository(context = context)

    @Test
    fun settingsAppTheme() = runTest {
        val onEachIndex = MutableSharedFlow<Int>()

        launch {
            repository.settings.collectIndexed { index, settingsData ->
                onEachIndex.emit(index)
                val expected = when (index) {
                    0 -> {
                        SettingsData(
                            appTheme = AppTheme.AS_SYSTEM
                        )
                    }
                    1 -> {
                        SettingsData(
                            appTheme = AppTheme.DARK
                        )
                    }
                    2 -> {
                        SettingsData(
                            appTheme = AppTheme.LIGHT
                        )
                    }
                    else -> {
                        cancel("Success")
                        SettingsData(
                            appTheme = AppTheme.AS_SYSTEM
                        )
                    }
                }
                assertEquals(expected, settingsData)
            }
        }

        launch {
            onEachIndex.collect { index ->
                when (index) {
                    0 -> { repository.setTheme(AppTheme.DARK) }
                    1 -> { repository.setTheme(AppTheme.LIGHT) }
                    2 -> {
                        repository.setTheme(AppTheme.AS_SYSTEM)
                        cancel("Success")
                    }
                }
            }
        }
    }
}