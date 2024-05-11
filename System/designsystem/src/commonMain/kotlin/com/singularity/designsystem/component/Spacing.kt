package com.singularity.designsystem.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun SmallSpacing() {
    Spacer(
        modifier = Modifier.size(com.singularity.designsystem.SmallSpacing)
    )
}

@Composable
fun MediumSpacing() {
    Spacer(
        modifier = Modifier.size(com.singularity.designsystem.MediumSpacing)
    )
}

@Composable
fun LargeSpacing() {
    Spacer(
        modifier = Modifier.size(com.singularity.designsystem.LargeSpacing)
    )
}

@Composable
fun ExtraLargeSpacing() {
    Spacer(
        modifier = Modifier.size(com.singularity.designsystem.ExtraLargeSpacing)
    )
}