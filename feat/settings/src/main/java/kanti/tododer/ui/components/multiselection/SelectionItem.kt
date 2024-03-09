package kanti.tododer.ui.components.multiselection

import androidx.compose.foundation.clickable
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
import kanti.tododer.ui.common.MultiSelectionStyle

@Composable
fun SelectionItem(
	strings: SelectionMenuStrings = SelectionMenuItemsDefaults.strings(),
	selectionStyles: Set<MultiSelectionStyle>,
	onSetSelectionStyles: (Set<MultiSelectionStyle>) -> Unit = {}
) {
	var showSelectionDialog by rememberSaveable { mutableStateOf(false) }
	ListItem(
		modifier = Modifier
			.clickable { showSelectionDialog = true },
		headlineContent = { Text(text = strings.selectionStyle) },
		supportingContent = {
			Text(
				text = selectionStyles.joinToString {
					strings.fromMultiSelectionStyle(it)
				}
			)
		},
		leadingContent = {
			Icon(painter = painterResource(id = R.drawable.multi_select), contentDescription = null)
		}
	)

	if (showSelectionDialog) {
		MultiSelectionDialog(
			onCloseDialog = { showSelectionDialog = false },
			initSet = selectionStyles,
			strings = strings,
			onSetSelectionStyles = onSetSelectionStyles
		)
	}
}

@Preview
@Composable
private fun PreviewSelectionItem() {
	SelectionItem(
		selectionStyles = setOf(MultiSelectionStyle.ColorFill)
	)
}