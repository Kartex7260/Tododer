package kanti.tododer.ui.components.dialogs

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kanti.tododer.feat.todo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetGroupDialog(
	onDismissRequest: () -> Unit = {},
	initialGroup: String?,
	groups: Set<String?> = setOf(),
	onSetGroup: (String?) -> Unit = {}
) {
	var groupName by rememberSaveable { mutableStateOf(initialGroup) }
	AlertDialog(
		onDismissRequest = onDismissRequest,
		confirmButton = {
			TextButton(onClick = {
				onSetGroup(null)
				onDismissRequest()
			}) {
				Text(text = stringResource(id = R.string.remove))
			}
			TextButton(
				onClick = {
					if (groupName?.isBlank() == true) {
						onSetGroup(null)
						return@TextButton
					}
					onSetGroup(groupName)
					onDismissRequest()
				}
			) {
				Text(text = stringResource(id = R.string.set))
			}
		},
		dismissButton = {
			TextButton(onClick = onDismissRequest) {
				Text(text = stringResource(id = R.string.cancel))
			}
		},
		title = {
			Text(text = stringResource(id = R.string.set_group))
		},
		text = {
			var expandedMenu by rememberSaveable { mutableStateOf(true) }
			ExposedDropdownMenuBox(
				expanded = expandedMenu,
				onExpandedChange = { expandedMenu = it }
			) {
				OutlinedTextField(
					modifier = Modifier
						.menuAnchor(),
					value = groupName ?: "",
					onValueChange = { groupName = it },
					trailingIcon = {
						IconButton(onClick = { expandedMenu = !expandedMenu }) {
							val animatedRotate by animateFloatAsState(
								targetValue = if (expandedMenu) 180f else 0f,
								label = "arrowDown"
							)
							Icon(
								modifier = Modifier
									.rotate(animatedRotate),
								imageVector = Icons.Default.KeyboardArrowDown,
								contentDescription = null
							)
						}
					},
					label = {
						Text(text = stringResource(id = R.string.group_name))
					}
				)

				ExposedDropdownMenu(
					expanded = expandedMenu,
					onDismissRequest = { expandedMenu = false }
				) {
					for (groupHint in groups) {
						DropdownMenuItem(
							text = {
								Text(
									text = groupHint ?: stringResource(id = R.string.without_group)
								)
							},
							onClick = {
								groupName = groupHint
								expandedMenu = false
							}
						)
					}
				}
			}
		}
	)
}

@Preview
@Composable
private fun PreviewSetGroupDialog() {
	val context = LocalContext.current
	SetGroupDialog(
		initialGroup = "Test",
		groups = setOf("Test", "Foo", "Do", null),
		onSetGroup = {
			Toast.makeText(context, "onSetGroup($it)", Toast.LENGTH_SHORT).show()
		}
	)
}