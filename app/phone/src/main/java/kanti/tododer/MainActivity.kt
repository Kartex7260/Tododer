package kanti.tododer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import kanti.tododer.ui.theme.TododerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	private val viewModel by viewModels<MainViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel.init()
		setContent {
			MainActivityContent()
		}
	}
}

@Composable
fun MainActivityContent() {
	TododerTheme {
		Surface(
			modifier = Modifier.fillMaxSize(),
			color = MaterialTheme.colorScheme.surface
		) {
			TododerNavHost(context = LocalContext.current)
		}
	}
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	MainActivityContent()
}