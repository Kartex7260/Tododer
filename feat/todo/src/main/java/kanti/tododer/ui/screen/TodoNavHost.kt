package kanti.tododer.ui.screen

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.screen.plan_list.PlanListScreen
import kanti.tododer.ui.screen.todo_detail.TodoDetailScreen
import kanti.tododer.ui.screen.todo_list.TodoListScreen

fun NavGraphBuilder.addTodoNavGraph(
	navController: NavController,
	todoListTobBarActions: @Composable () -> Unit = {},
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
				navArgument(todoDetailTodoIdParam) {
					type = NavType.IntType
				}
			)
		) {
			TodoDetailScreen(
				navController = navController,
				todoId = it.arguments?.getInt(todoDetailTodoIdParam) ?: 0
			)
		}

		composable(
			route = context.getString(R.string.nav_destination_plans)
		) {
			PlanListScreen(
				navController = navController,
				topBarActions = todoListTobBarActions
			)
		}
	}
}