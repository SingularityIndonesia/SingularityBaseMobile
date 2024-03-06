package com.singularityindonesia.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val TextDefault = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal,
    letterSpacing = 0.sp
)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextDefault.copy(
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextDefault.copy(
        fontSize = 45.sp,
        lineHeight = 52.sp,
    ),
    displaySmall = TextDefault.copy(
        fontSize = 36.sp,
        lineHeight = 44.sp,
    ),
    headlineLarge = TextDefault.copy(
        fontSize = 32.sp,
        lineHeight = 40.sp,
    ),
    headlineMedium = TextDefault.copy(
        fontSize = 28.sp,
        lineHeight = 36.sp,
    ),
    headlineSmall = TextDefault.copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),
    titleLarge = TextDefault.copy(
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    titleMedium = TextDefault.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    titleSmall = TextDefault.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (0.1).sp
    ),
    bodyLarge = TextDefault.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (0.5).sp
    ),
    bodyMedium = TextDefault.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (0.25).sp
    ),
    bodySmall = TextDefault.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelLarge = TextDefault.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (0.1).sp
    ),
    labelMedium = TextDefault.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = (0.5).sp
    ),
    labelSmall = TextDefault.copy(
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = (0.5).sp
    ),

)