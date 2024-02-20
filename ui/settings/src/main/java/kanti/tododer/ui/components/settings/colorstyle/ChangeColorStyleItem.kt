package kanti.tododer.ui.components.settings.colorstyle

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import kanti.tododer.ui.components.settings.R
import kanti.tododer.ui.components.settings.SettingsMenuItem

@Composable
fun ChangeColorStyleItem(
    modifier: Modifier = Modifier,
    state: ColorStyleItemUiState,
    strings: ChangeColorStyleStrings = ChangeColorStyleItemDefaults.strings(),
    onChangeStyle: (id: Int) -> Unit = {}
) {
    val currentColorStyle = remember(key1 = state) {
        state.colorStylesData.first { it.id == state.currentStyleId }
    }
    SettingsMenuItem(
        modifier = modifier,
        headlineContent = { Text(text = strings.colorStyle) },
        supportingContent = {
            val name = strings.getByType(currentColorStyle.type) ?: currentColorStyle.name
            Text(text = name)
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_palette_24),
                contentDescription = null
            )
        },
        dropdownMenuContent = { onClose ->
            for (colorStyle in state.colorStylesData) {
                DropdownMenuItem(
                    text = { Text(text = strings.getByType(colorStyle.type) ?: colorStyle.name) },
                    onClick = {
                        onChangeStyle(colorStyle.id)
                        onClose()
                    }
                )
            }
        }
    )
}

@Preview
@Composable
private fun PreviewChangeColorStyleItem() {
    var currentStyleId by remember {
        mutableIntStateOf(1)
    }
    ChangeColorStyleItem(
        state = ColorStyleItemUiState(
            currentStyleId = currentStyleId,
            colorStylesData = listOf(
                ColorStyleDataUiState(1, "Black"),
                ColorStyleDataUiState(2, "White"),
                ColorStyleDataUiState(3, "Test")
            )
        ),
        onChangeStyle = { currentStyleId = it    }
    )
}