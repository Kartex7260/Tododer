package kanti.tododer

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kanti.tododer.ui.screen.addTodoNavGraph
import kanti.tododer.ui.screen.settingsNavGraph

@Composable
fun TododerNavHost(
	context: Context
) {
	val navController = rememberNavController()

	NavHost(
		navController = navController,
		startDestination = getStartDestination()
	) {
		addTodoNavGraph(
			navController = navController,
			optionMenuItems = { closeMenu ->
				DropdownMenuItem(
					text = {
						Text(
							text = stringResource(id = kanti.tododer.feat.settings.R.string.settings)
						)
					},
					onClick = {
						val settingsRoute = context.getString(kanti.tododer.feat.settings.R.string.settings_route)
						navController.navigate(route = settingsRoute)
						closeMenu()
					},
					leadingIcon = {
						Icon(imageVector = Icons.Default.Settings, contentDescription = null)
					}
				)
			},
			context = context
		)
		settingsNavGraph(
			navController = navController,
			context = context
		)
	}
}

@Composable
private fun getStartDestination(): String {
	return stringResource(id = kanti.tododer.feat.todo.R.string.nav_destination_todo)
}