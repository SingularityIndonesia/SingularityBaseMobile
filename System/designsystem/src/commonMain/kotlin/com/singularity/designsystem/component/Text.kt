package com.singularity.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp

@Composable
fun TextTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(
                vertical = 16.dp
            )
            .then(modifier)
    )
}

@Composable
fun TextSubTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontStyle = FontStyle.Italic,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .padding(
                vertical = 4.dp
            )
            .alpha(.9f)
            .then(modifier)
    )
}

@Composable
fun TextHeadline1(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier
            .padding(
                vertical = 20.dp
            )
            .then(modifier)
    )
}

@Composable
fun TextHeadline2(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier
            .padding(
                vertical = 14.dp
            )
            .then(modifier)
    )
}

@Composable
fun TextHeadline3(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier
            .padding(
                vertical = 12.dp
            )
            .then(modifier)
    )
}

@Composable
fun TextBody(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .padding(
                vertical = 2.dp
            )
            .then(modifier)
    )
}

@Composable
fun TextLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier
            .then(modifier)
    )
}