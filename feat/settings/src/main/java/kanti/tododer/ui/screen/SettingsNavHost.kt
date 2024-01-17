package kanti.tododer.ui.screen

import android.content.Context
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kanti.tododer.feat.settings.R
import kanti.tododer.ui.screen.main.SettingsMainScreen

fun NavGraphBuilder.settingsNavGraph(
	navController: NavController,
	context: Context
) {
	navigation(
		startDestination = context.getString(R.string.settings_main),
		route = context.getString(R.string.settings_route)
	) {
		composable(
			route = context.getString(R.string.settings_main),
			enterTransition = { slideInHorizontally { it } },
			exitTransition = { slideOutHorizontally { it } }
		) {
			SettingsMainScreen(
				navController = navController
			)
		}
	}
}