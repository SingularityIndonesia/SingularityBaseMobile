/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TextTitle(
    text: String,
    color: Color? = null,
    textAlign: TextAlign = TextAlign.Left,
    modifier: Modifier = Modifier
) {
    SelectionContainer {
        if (color == null)
            Text(
                text = text,
                textAlign = textAlign,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(
                        vertical = 16.dp
                    )
                    .then(modifier)
            )
        else
            Text(
                text = text,
                color = color,
                textAlign = textAlign,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(
                        vertical = 16.dp
                    )
                    .then(modifier)
            )
    }
}

@Composable
fun TextSubTitle(
    text: String,
    color: Color? = null,
    textAlign: TextAlign = TextAlign.Left,
    modifier: Modifier = Modifier
) {
    SelectionContainer {
        if (color == null)
            Text(
                text = text,
                textAlign = textAlign,
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(
                        vertical = 4.dp
                    )
                    .alpha(.9f)
                    .then(modifier)
            )
        else
            Text(
                text = text,
                textAlign = textAlign,
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
}

@Composable
fun TextHeadline1(
    text: String,
    color: Color? = null,
    textAlign: TextAlign = TextAlign.Left,
    modifier: Modifier = Modifier
) {
    SelectionContainer {
        if (color == null)
            Text(
                text = text,
                textAlign = textAlign,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(
                        vertical = 20.dp
                    )
                    .then(modifier)
            )
        else
            Text(
                text = text,
                color = color,
                textAlign = textAlign,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(
                        vertical = 20.dp
                    )
                    .then(modifier)
            )
    }
}

@Composable
fun TextHeadline2(
    text: String,
    color: Color? = null,
    textAlign: TextAlign = TextAlign.Left,
    modifier: Modifier = Modifier
) {
    SelectionContainer {
        if (color == null)
            Text(
                text = text,
                textAlign = textAlign,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(
                        vertical = 14.dp
                    )
                    .then(modifier)
            )
        else
            Text(
                text = text,
                textAlign = textAlign,
                color = color,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(
                        vertical = 14.dp
                    )
                    .then(modifier)
            )
    }

}

@Composable
fun TextHeadline3(
    text: String,
    color: Color? = null,
    textAlign: TextAlign = TextAlign.Left,
    modifier: Modifier = Modifier
) {
    SelectionContainer {
        if (color == null)
            Text(
                text = text,
                textAlign = textAlign,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(
                        vertical = 12.dp
                    )
                    .then(modifier)
            )
        else
            Text(
                text = text,
                textAlign = textAlign,
                color = color,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(
                        vertical = 12.dp
                    )
                    .then(modifier)
            )
    }

}

@Composable
fun TextBody(
    text: String,
    color: Color? = null,
    textAlign: TextAlign = TextAlign.Left,
    modifier: Modifier = Modifier
) {
    SelectionContainer {
        if (color == null)
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = textAlign,
                modifier = Modifier
                    .padding(
                        vertical = 2.dp
                    )
                    .then(modifier)
            )
        else
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = textAlign,
                color = color,
                modifier = Modifier
                    .padding(
                        vertical = 2.dp
                    )
                    .then(modifier)
            )
    }
}

@Composable
fun TextLabel(
    text: String,
    textAlign: TextAlign = TextAlign.Left,
    color: Color? = null,
    modifier: Modifier = Modifier
) {
    SelectionContainer {
        if (color == null)
            Text(
                text = text,
                textAlign = textAlign,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .then(modifier)
            )
        else
            Text(
                text = text,
                color = color,
                textAlign = textAlign,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .then(modifier)
            )
    }
}