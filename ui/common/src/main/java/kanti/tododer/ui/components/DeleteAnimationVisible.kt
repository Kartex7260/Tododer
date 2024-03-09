package kanti.tododer.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DeleteAnimationVisible(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    content: @Composable AnimatedVisibilityScope.() -> Unit
)  = AnimatedVisibility(
    modifier = modifier,
    visible = visible,
    enter = scaleIn() + expandVertically() + fadeIn(),
    exit = scaleOut() + shrinkVertically() + fadeOut(),
    content = content
)