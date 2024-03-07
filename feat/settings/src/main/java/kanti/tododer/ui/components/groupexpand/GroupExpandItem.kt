package kanti.tododer.ui.components.groupexpand

import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kanti.tododer.feat.settings.R

@Composable
fun GroupExpandItem(
	groupExpandDefault: Boolean,
	onChangeExpandDefault: (Boolean) -> Unit = {}
) = ListItem(
	headlineContent = { Text(text = stringResource(id = R.string.group_expansion)) },
	leadingContent = {
		Icon(painter = painterResource(id = R.drawable.segment), contentDescription = null)
	},
	trailingContent = {
		Switch(checked = groupExpandDefault, onCheckedChange = onChangeExpandDefault)
	}
)

@Preview
@Composable
private fun PreviewGroupExpandItem() {
	var expandDefault by remember { mutableStateOf(true) }
	GroupExpandItem(
		groupExpandDefault = expandDefault,
		onChangeExpandDefault = { expandDefault = it }
	)
}