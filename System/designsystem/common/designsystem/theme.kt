/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val LightColorScheme = lightColorScheme(
    primary = HawkingRadiation[60]!!,
    onPrimary = HawkingRadiation[100]!!,
    primaryContainer = HawkingRadiation[90]!!,
    onPrimaryContainer = HawkingRadiation[10]!!,
    
    secondary = HawkingDimRadiation[40]!!,
    onSecondary = HawkingDimRadiation[100]!!,
    secondaryContainer = HawkingDimRadiation[90]!!,
    onSecondaryContainer = HawkingDimRadiation[10]!!,
    
    tertiary = HighEnergyPhoton[40]!!,
    onTertiary = HighEnergyPhoton[100]!!,
    tertiaryContainer = HighEnergyPhoton[90]!!,
    onTertiaryContainer = HighEnergyPhoton[10]!!,
    
    error = Error[40]!!,
    onError = Error[100]!!,
    errorContainer = Error[90]!!,
    onErrorContainer = Error[10]!!,
    
    surfaceDim = SpaceDark[87]!!,
    surface = SpaceDark[98]!!,
    surfaceBright = SpaceDark[98]!!,
    onSurface = SpaceDark[10]!!,
    
    surfaceContainerLowest = SpaceDark[100]!!,
    surfaceContainerLow = SpaceDark[96]!!,
    surfaceContainer = SpaceDark[94]!!,
    surfaceContainerHigh = SpaceDark[92]!!,
    surfaceContainerHighest = SpaceDark[90]!!,
    
    // surfaceVariant = SpaceDarkVariant[90]!!,
    onSurfaceVariant = SpaceDarkVariant[30]!!,
    
    outline = SpaceDarkVariant[50]!!,
    outlineVariant = SpaceDarkVariant[80]!!,
    
    inverseSurface = SpaceDark[20]!!,
    inverseOnSurface = SpaceDark[95]!!,
    inversePrimary = HawkingRadiation[80]!!,
    
    scrim = SpaceDark[0]!!,
)

private val DarkColorScheme = darkColorScheme(
    primary = HawkingRadiation[80]!!,
    onPrimary = HawkingRadiation[20]!!,
    primaryContainer = HawkingRadiation[30]!!,
    onPrimaryContainer = HawkingRadiation[90]!!,
    
    secondary = HawkingDimRadiation[80]!!,
    onSecondary = HawkingDimRadiation[20]!!,
    secondaryContainer = HawkingDimRadiation[30]!!,
    onSecondaryContainer = HawkingDimRadiation[90]!!,
    
    tertiary = HighEnergyPhoton[80]!!,
    onTertiary = HighEnergyPhoton[20]!!,
    tertiaryContainer = HighEnergyPhoton[30]!!,
    onTertiaryContainer = HighEnergyPhoton[90]!!,
    
    error = Error[90]!!,
    onError = Error[20]!!,
    errorContainer = Error[30]!!,
    onErrorContainer = Error[90]!!,
    
    surfaceDim = SpaceDark[6]!!,
    surface = SpaceDark[6]!!,
    surfaceBright = SpaceDark[24]!!,
    onSurface = SpaceDark[90]!!,
    
    surfaceContainerLowest = SpaceDark[4]!!,
    surfaceContainerLow = SpaceDark[10]!!,
    surfaceContainer = SpaceDark[12]!!,
    surfaceContainerHigh = SpaceDark[17]!!,
    surfaceContainerHighest = SpaceDark[22]!!,
    
    // surfaceVariant = SpaceDarkVariant[6]!!,
    onSurfaceVariant = SpaceDarkVariant[80]!!,
    
    outline = SpaceDarkVariant[60]!!,
    outlineVariant = SpaceDarkVariant[30]!!,
    
    inverseSurface = SpaceDark[90]!!,
    inverseOnSurface = SpaceDark[20]!!,
    inversePrimary = HawkingRadiation[40]!!,
    
    scrim = SpaceDark[0]!!,
)

@Composable
fun SingularityTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (darkTheme)
            DarkColorScheme
        else
            LightColorScheme

    MaterialTheme (
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}