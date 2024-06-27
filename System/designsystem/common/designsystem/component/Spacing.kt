/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun SmallSpacing() {
    Spacer(
        modifier = Modifier.size(designsystem.SmallSpacing)
    )
}

@Composable
fun MediumSpacing() {
    Spacer(
        modifier = Modifier.size(designsystem.MediumSpacing)
    )
}

@Composable
fun LargeSpacing() {
    Spacer(
        modifier = Modifier.size(designsystem.LargeSpacing)
    )
}

@Composable
fun ExtraLargeSpacing() {
    Spacer(
        modifier = Modifier.size(designsystem.ExtraLargeSpacing)
    )
}

@Composable
fun ParagraphSpacing() {
    Spacer(
        modifier = Modifier.size(designsystem.ParagraphSpacing)
    )
}

@Composable
fun ColumnScope.Expand() {
    Spacer(
        modifier = Modifier.weight(1f)
    )
}

@Composable
fun RowScope.Expand() {
    Spacer(
        modifier = Modifier.weight(1f)
    )
}