package kanti.tododer.ui.fragments.screens.todo_detail

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import kanti.tododer.R

class TodoDetailMenuProvider(
	private val navController: NavController,
	private val settingsNavDirections: NavDirections,
	private val delete: () -> Unit
) : MenuProvider {

	override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
		menuInflater.inflate(R.menu.menu_base, menu)
	}

	override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
		R.id.menu_base_settings -> {
			navController.navigate(settingsNavDirections)
			true
		}
		else -> false
	}

}