package system.designsystem.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun SmallSpacing() {
    Spacer(
        modifier = Modifier.size(system.designsystem.SmallSpacing)
    )
}

@Composable
fun MediumSpacing() {
    Spacer(
        modifier = Modifier.size(system.designsystem.MediumSpacing)
    )
}

@Composable
fun LargeSpacing() {
    Spacer(
        modifier = Modifier.size(system.designsystem.LargeSpacing)
    )
}

@Composable
fun ExtraLargeSpacing() {
    Spacer(
        modifier = Modifier.size(system.designsystem.ExtraLargeSpacing)
    )
}