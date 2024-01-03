package kanti.tododer.ui.screen

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import kanti.tododer.ui.screen.todo_list.R
import kanti.tododer.ui.screen.todo_list.TodoListScreen

fun NavGraphBuilder.addTodoNavGraph(
	navController: NavController,
	todoListNavigationIcon: (@Composable () -> Unit)? = null,
	todoListTobBarActions: (@Composable () -> Unit)? = null,
	context: Context
) {
	navigation(
		startDestination = context.getString(R.string.nav_destination_todos),
		route = context.getString(R.string.nav_destination_todo)
	) {
		composable(
			route = context.getString(R.string.nav_destination_todos)
		) {
			TodoListScreen(
				navController = navController,
				topBarActions = todoListTobBarActions
			)
		}

		val todoDetailTodoIdParam = context.getString(R.string.nav_destination_todo_detail_todo_id_param)
		composable(
			route = "${context.getString(R.string.nav_destination_todo_detail)}?" +
					"$todoDetailTodoIdParam={$todoDetailTodoIdParam}",
			arguments = listOf(
				navArgument(todoDetailTodoIdParam) { nullable = true }
			)
		) {
		}

		composable(
			route = context.getString(R.string.nav_destination_plans)
		) {
		}
	}
}