package kanti.tododer.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.android.material.color.DynamicColors
import kanti.tododer.data.colorstyle.ColorStyle
import kanti.tododer.data.colorstyle.DefaultColorStyles

private fun ColorStyle.toColorScheme(dark: Boolean): ColorScheme {
	return when (dark) {
		true -> {
			darkColorScheme(
				primary = Color(primaryDark),
				onPrimary = Color(onPrimaryDark),
				primaryContainer = Color(primaryContainerDark),
				onPrimaryContainer = Color(onPrimaryContainerDark),

				secondary = Color(secondaryDark),
				onSecondary = Color(onSecondaryDark),
				secondaryContainer = Color(secondaryContainerDark),
				onSecondaryContainer = Color(onSecondaryContainerDark),

				tertiary = Color(tertiaryDark),
				onTertiary = Color(onTertiaryDark),
				tertiaryContainer = Color(tertiaryContainerDark),
				onTertiaryContainer = Color(onTertiaryContainerDark),

				error = Color(errorDark),
				onError = Color(onErrorDark),
				errorContainer = Color(errorContainerDark),
				onErrorContainer = Color(onErrorContainerDark),

				background = Color(backgroundDark),
				onBackground = Color(onBackgroundDark),
				surface = Color(surfaceDark),
				onSurface = Color(onSurfaceDark),
				surfaceVariant = Color(surfaceVariantDark),
				onSurfaceVariant = Color(onSurfaceVariantDark),

				outline = Color(outlineDark)
			)
		}
		false -> {
			lightColorScheme(
				primary = Color(primaryLight),
				onPrimary = Color(onPrimaryLight),
				primaryContainer = Color(primaryContainerLight),
				onPrimaryContainer = Color(onPrimaryContainerLight),

				secondary = Color(secondaryLight),
				onSecondary = Color(onSecondaryLight),
				secondaryContainer = Color(secondaryContainerLight),
				onSecondaryContainer = Color(onSecondaryContainerLight),

				tertiary = Color(tertiaryLight),
				onTertiary = Color(onTertiaryLight),
				tertiaryContainer = Color(tertiaryContainerLight),
				onTertiaryContainer = Color(onTertiaryContainerLight),

				error = Color(errorLight),
				onError = Color(onErrorLight),
				errorContainer = Color(errorContainerLight),
				onErrorContainer = Color(onErrorContainerLight),

				background = Color(backgroundLight),
				onBackground = Color(onBackgroundLight),
				surface = Color(surfaceLight),
				onSurface = Color(onSurfaceLight),
				surfaceVariant = Color(surfaceVariantLight),
				onSurfaceVariant = Color(onSurfaceVariantLight),

				outline = Color(outlineLight)
			)
		}
	}
}

@Composable
fun TododerTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	colorStyle: ColorStyle? = null,
	content: @Composable () -> Unit
) {
//	val systemUiController = rememberSystemUiController()
//	systemUiController.isSystemBarsVisible = false
	val colorScheme = when {
		colorStyle == null && DynamicColors.isDynamicColorAvailable() -> {
			val context = LocalContext.current
			if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
		}
		else -> colorStyle?.toColorScheme(darkTheme)
			?: DefaultColorStyles.Standard.toColorScheme(darkTheme)
	}
	val view = LocalView.current
	if (!view.isInEditMode) {
		SideEffect {
			val window = (view.context as Activity).window
			window.statusBarColor = colorScheme.primary.toArgb()
			WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
		}
	}

	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		content = content
	)
}