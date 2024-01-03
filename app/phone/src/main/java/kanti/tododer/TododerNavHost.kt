package kanti.tododer

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kanti.tododer.ui.screen.addTodoNavGraph

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
			context = context
		)
	}
}

@Composable
private fun getStartDestination(): String {
	return stringResource(id = kanti.tododer.ui.screen.todo_list.R.string.nav_destination_todo)
}