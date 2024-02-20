package kanti.tododer.ui.components.settings.theme

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import kanti.tododer.ui.components.settings.R
import kanti.tododer.ui.components.settings.SettingsMenuItem

@Composable
fun ChangeThemeItem(
    modifier: Modifier = Modifier,
    state: ThemeSettingsUiState,
    strings: ChangeThemeItemStrings = ChangeThemeItemDefaults.strings(),
    onThemeChanged: ((ThemeSettingsUiState) -> Unit) = {}
) = SettingsMenuItem(
    modifier = modifier,
    headlineContent = { Text(text = strings.appTheme) },
    supportingContent = {
        Text(
            text = when (state) {
                ThemeSettingsUiState.AS_SYSTEM -> strings.themeAsSystem
                ThemeSettingsUiState.LIGHT -> strings.themeLight
                ThemeSettingsUiState.DARK -> strings.themeDark
            }
        )
    },
    leadingContent = {
        Icon(
            painter = painterResource(id = R.drawable.baseline_dark_mode_24),
            contentDescription = null
        )
    },
    dropdownMenuContent = { onClose ->
        DropdownMenuItem(
            text = { Text(text = strings.themeAsSystem) },
            onClick = {
                onThemeChanged(ThemeSettingsUiState.AS_SYSTEM)
                onClose()
            }
        )
        DropdownMenuItem(
            text = { Text(text = strings.themeLight) },
            onClick = {
                onThemeChanged(ThemeSettingsUiState.LIGHT)
                onClose()
            }
        )
        DropdownMenuItem(
            text = { Text(text = strings.themeDark) },
            onClick = {
                onThemeChanged(ThemeSettingsUiState.DARK)
                onClose()
            }
        )
    }
)

@Preview
@Composable
private fun PreviewThemeSettingsItem() {
    var theme by remember {
        mutableStateOf(ThemeSettingsUiState.AS_SYSTEM)
    }
    ChangeThemeItem(
        state = theme,
        onThemeChanged = { theme = it }
    )
}
