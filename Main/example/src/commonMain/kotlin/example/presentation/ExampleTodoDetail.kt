/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package example.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.singularity.lifecycle.SaveAbleState

data class ExampleTodoDetailScreenPld(
    val id: String
)

@Composable
fun ExampleTodoDetailScreen(
    pld: ExampleTodoDetailScreenPld,
    saveAbleState: SaveAbleState
) {
    Text(
        """
            This is detail screen for Todo with id = ${pld.id}.
            I'm too lazy to do it.
            But this is enough to demonstrate navigation.
        """.trimIndent()
        
    )
}