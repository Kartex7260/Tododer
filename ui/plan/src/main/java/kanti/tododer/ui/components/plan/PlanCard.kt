package kanti.tododer.ui.components.plan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kanti.fillingprogressbar.FillingProgressBar

@Composable
fun PlanCard(
	modifier: Modifier = Modifier,
	planData: PlanData,
	onClick: () -> Unit,
	endButton: @Composable () -> Unit = {}
) = Card(
	modifier = modifier
		.clickable(onClick = onClick)
		.height(56.dp)
) {
	Column(
		modifier = Modifier
			.padding(
				start = 8.dp,
				end = 4.dp,
				top = 8.dp,
				bottom = 8.dp
			)
			.fillMaxSize(),
		verticalArrangement = Arrangement.Center
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth()
		) {
			FillingProgressBar(
				progress = planData.progress,
				contentPadding = PaddingValues(
					start = 24.dp,
					end = 24.dp
				),

			)

			Spacer(modifier = Modifier.width(width = 4.dp))

			Text(
				modifier = Modifier.weight(1f),
				text = planData.title,
				style = MaterialTheme.typography.titleMedium,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)

			Spacer(modifier = Modifier.width(width = 8.dp))

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
private fun PreviewPlanCardAll(
	@PreviewParameter(PreviewPlanCardParameterProvider::class) plan: PlanData
) {
	PlanCard(
		modifier = Modifier
			.padding(all = 16.dp)
			.fillMaxWidth(),
		planData = plan,
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
private fun PreviewPlanCardAllWithoutActions(
	@PreviewParameter(PreviewPlanCardParameterProvider::class) plan: PlanData
) {
	PlanCard(
		modifier = Modifier
			.padding(all = 16.dp)
			.fillMaxWidth(),
		planData = plan,
		onClick = {  }
	)
}

private class PreviewPlanCardParameterProvider : PreviewParameterProvider<PlanData> {

	override val values: Sequence<PlanData> = sequenceOf(
		PlanData(
			id = 1,
			title = "State test"
		),
		PlanData(
			id = 2,
			title = "Very large title, its over long title, very long"
		)
	)
}