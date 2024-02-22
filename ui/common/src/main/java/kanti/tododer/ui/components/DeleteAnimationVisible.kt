package kanti.tododer.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DeleteAnimationVisible(
    visible: Boolean = true,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) = AnimatedVisibility(
    modifier = Modifier,
    visible = visible,
    enter = scaleIn(),
    exit = scaleOut(),
    content = content
)