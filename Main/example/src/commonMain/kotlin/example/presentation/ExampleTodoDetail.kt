/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package example.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.singularity.designsystem.LargePadding
import com.singularity.designsystem.component.LargeSpacing
import com.singularity.designsystem.component.TopAppBar
import com.singularity.lifecycle.SaveAbleState

data class ExampleTodoDetailScreenPld(
    val id: String,
    val onBack: () -> Unit
)

@Composable
fun ExampleTodoDetailScreen(
    pld: ExampleTodoDetailScreenPld,
    saveAbleState: SaveAbleState
) {
    Column {
        TopAppBar(
            "Todo List Title",
            onBack = pld.onBack
        )
        LargeSpacing()
        Text(
            """
            This is detail screen for Todo with id = ${pld.id}.
            I'm too lazy to do it.
            But this is enough to demonstrate navigation.
        """.trimIndent(),
            modifier = Modifier.padding(
                horizontal = LargePadding
            )
        )
    }
}