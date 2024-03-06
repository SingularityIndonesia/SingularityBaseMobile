package com.singularityindonesia.designsystem.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SmallSpacing() {
    Spacer(modifier = Modifier.size(4.dp))
}

@Composable
fun MediumSpacing() {
    Spacer(modifier = Modifier.size(8.dp))
}

@Composable
fun LargeSpacing() {
    Spacer(modifier = Modifier.size(16.dp))
}

@Composable
fun ExtraLargeSpacing() {
    Spacer(modifier = Modifier.size(24.dp))
}
