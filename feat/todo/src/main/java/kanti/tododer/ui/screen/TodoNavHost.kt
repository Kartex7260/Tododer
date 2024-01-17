package kanti.tododer.ui.screen

import android.content.Context
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
	optionMenuItems: (@Composable (closeMenu: () -> Unit) -> Unit)? = null,
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
				optionMenuItems = optionMenuItems
			)
		}

		val todoDetailTodoIdParam = context.getString(R.string.nav_destination_todo_detail_todo_id_param)
		composable(
			route = "${context.getString(R.string.nav_destination_todo_detail)}/{$todoDetailTodoIdParam}",
			arguments = listOf(
				navArgument(todoDetailTodoIdParam) { type = NavType.LongType }
			),
			enterTransition = { slideInHorizontally { it } },
			exitTransition = { slideOutHorizontally { it } }
		) {
			TodoDetailScreen(
				navController = navController,
				todoId = it.arguments?.getLong(todoDetailTodoIdParam) ?: 0
			)
		}

		composable(
			route = context.getString(R.string.nav_destination_plans),
			enterTransition = { slideInHorizontally { -it } },
			exitTransition = { slideOutHorizontally { -it } }
		) {
			PlanListScreen(
				navController = navController,
				optionMenuItems = optionMenuItems
			)
		}
	}
}