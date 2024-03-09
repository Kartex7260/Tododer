package kanti.tododer.ui.components.multiselection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kanti.tododer.ui.common.MultiSelectionStyle

@Composable
fun MultiSelectionDialog(
	onCloseDialog: () -> Unit = {},
	initSet: Set<MultiSelectionStyle>,
	strings: SelectionMenuStrings = SelectionMenuItemsDefaults.strings(),
	onSetSelectionStyles: (Set<MultiSelectionStyle>) -> Unit = {}
) {
	AlertDialog(
		onDismissRequest = { onCloseDialog() },
		confirmButton = {
			TextButton(onClick = onCloseDialog) {
				Text(text = strings.close)
			}
		},
		title = { Text(text = strings.setSelectionStyle) },
		text = {
			Column {
				val selector by rememberSaveable(
					stateSaver = Saver(
						save = { selector ->
							selector.selected.value.map { it.name }.toTypedArray()
						},
						restore = { styles ->
							val values = styles.map { MultiSelectionStyle.valueOf(it) }.toSet()
							Selector(values)
						}
					)
				) { mutableStateOf(Selector(initSet)) }

				LaunchedEffect(key1 = selector) {
					selector.selected.collect {
						onSetSelectionStyles(it)
					}
				}

				val selectedSet by selector.selected.collectAsState()

				for (mss in MultiSelectionStyle.entries) {
					val selected = selectedSet.contains(mss)
					val enabled = !(selected && selectedSet.size == 1)
					Row(
						modifier = Modifier
							.height(height = 56.dp)
							.fillMaxWidth()
							.clickable(enabled = enabled) {
								selector.setSelect(mss, !selected)
							},
						verticalAlignment = Alignment.CenterVertically
					) {
						Checkbox(
							checked = selected,
							onCheckedChange = { selector.setSelect(mss, it) },
							enabled = enabled,
							colors = CheckboxDefaults.colors(
								checkmarkColor = MaterialTheme.colorScheme
									.surfaceVariant,

							)
						)
						Spacer(modifier = Modifier.width(width = 4.dp))
						Text(
							text = strings.fromMultiSelectionStyle(mss),
							style = MaterialTheme.typography.bodyLarge
						)
					}
				}
			}
		}
	)
}

@Preview
@Composable
private fun PreviewMultiSelectionDialog() {
	MultiSelectionDialog(
		initSet = setOf(MultiSelectionStyle.Checkbox)
	)
}