package kanti.tododer

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import dagger.hilt.android.HiltAndroidApp
import kanti.tododer.common.Const

@HiltAndroidApp
class TododerApplication : Application() {

	override fun onCreate() {
		super.onCreate()
		Const.init(this)
		DynamicColors.applyToActivitiesIfAvailable(
			this,
			DynamicColorsOptions.Builder()
				.build()
		)
	}

}