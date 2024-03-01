package kanti.tododer.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import kanti.tododer.feat.settings.R

@Composable
fun SettingsMenuItem(
    modifier: Modifier = Modifier,
    headlineContent: @Composable () -> Unit,
    supportingContent: (@Composable () -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    dropdownMenuContent: @Composable ColumnScope.(onClose: () -> Unit) -> Unit = {}
) {
    var expandThemeMenu by rememberSaveable { mutableStateOf(false) }
    ListItem(
        modifier = Modifier
            .clickable { expandThemeMenu = true }
            .then(modifier),
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        leadingContent = leadingContent
    )
    val closeMenu = {
        expandThemeMenu = false
    }
    DropdownMenu(
        expanded = expandThemeMenu,
        onDismissRequest = { closeMenu() },
        content = {
            dropdownMenuContent(closeMenu)
        }
    )
}

@Preview
@Composable
private fun PreviewSettingsMenuItem() {
    SettingsMenuItem(
        headlineContent = {
            Text(text = "Test")
        },
        supportingContent = {
            Text(text = "Supporting text")
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_dark_mode_24),
                contentDescription = null
            )
        },
        dropdownMenuContent = { onClose ->
            DropdownMenuItem(
                text = { Text(text = "Menu item") },
                onClick = { onClose() }
            )
        }
    )
}