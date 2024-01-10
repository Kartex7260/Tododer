package kanti.tododer.ui.components.plan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import knati.fillingprogressbar.FillingProgressBar

@Composable
fun PlanCard(
	modifier: Modifier = Modifier,
	planData: PlanData,
	onClick: () -> Unit,
	endButton: @Composable () -> Unit = {}
) = OutlinedCard(
	modifier = modifier
		.clickable { onClick() }
) {
	Column(
		modifier = Modifier
			.padding(
				start = 16.dp,
				end = 4.dp,
				top = 12.dp,
				bottom = 12.dp
			)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth()
		) {
			FillingProgressBar(
				progress = planData.progress
			)

			Spacer(modifier = Modifier.width(width = 16.dp))

			Text(
				modifier = Modifier.weight(1f),
				text = planData.title,
				style = MaterialTheme.typography.titleLarge,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)

			Spacer(modifier = Modifier.width(width = 16.dp))

			Box {
				endButton()
			}
		}
	}
}

@Preview(
	showBackground = true
)
@Composable
private fun PreviewPlanCardAll() {
	PlanCard(
		modifier = Modifier
			.padding(all = 16.dp),
		planData = PlanData(
			id = -1,
			title = "All",
			progress = .35f
		),
		onClick = {  }
	) {
		IconButton(onClick = {  }) {
			Icon(
				imageVector = Icons.Default.MoreVert,
				contentDescription = null
			)
		}
	}
}

@Preview(
	showBackground = true
)
@Composable
private fun PreviewPlanCardDefault() {
	PlanCard(
		modifier = Modifier
			.padding(all = 16.dp),
		planData = PlanData(
			id = -2,
			title = "Default",
			progress = .6f
		),
		onClick = {  }
	) {
		IconButton(onClick = {  }) {
			Icon(
				imageVector = Icons.Default.MoreVert,
				contentDescription = null
			)
		}
	}
}

@Preview(
	showBackground = true
)
@Composable
private fun PreviewPlanCardCustom() {
	PlanCard(
		modifier = Modifier
			.padding(all = 16.dp),
		planData = PlanData(
			id = 1,
			title = "This is a long text for testin plan card",
			progress = 1.0f
		),
		onClick = {  }
	) {
		IconButton(onClick = {  }) {
			Icon(
				imageVector = Icons.Default.MoreVert,
				contentDescription = null
			)
		}
	}
}