package kanti.tododer.ui.components.grouping

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GroupHeader(
	modifier: Modifier = Modifier,
	title: String,
	expanded: Boolean = false,
	onChangeExpand: (Boolean) -> Unit = {}
) {
	Row(
		modifier = Modifier
			.clip(ButtonDefaults.shape)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				role = Role.Button,
				indication = rememberRipple(),
				onClick = { onChangeExpand(!expanded) }
			)
			.then(modifier),
		verticalAlignment = Alignment.CenterVertically
	) {
		Spacer(modifier = Modifier.width(width = 8.dp))
		Text(
			text = title,
			style = MaterialTheme.typography.headlineSmall.copy(
				platformStyle = PlatformTextStyle(
					includeFontPadding = false,
				)
			)
		)
		Spacer(modifier = Modifier.width(width = 8.dp))
		val animatedRotate by animateFloatAsState(
			targetValue = if (expanded) 180f else 0f,
			label = "animateArrowRotate"
		)
		Box(
			modifier = Modifier.size(40.dp),
			contentAlignment = Alignment.Center
		) {
			Icon(
				modifier = Modifier
					.rotate(animatedRotate),
				imageVector = Icons.Default.KeyboardArrowDown,
				contentDescription = null
			)
		}
	}
}

@Preview
@Composable
private fun PreviewGroupHeader() {
	var expanded by remember { mutableStateOf(false) }
	GroupHeader(
		title = "Group 1",
		expanded = expanded,
		onChangeExpand = { expanded = it }
	)
}