/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package main.example.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import system.designsystem.LargePadding
import system.designsystem.component.TextBody
import system.designsystem.component.TextHeadline1
import system.designsystem.component.TextHeadline2
import system.designsystem.component.TextHeadline3
import system.designsystem.component.TextSubTitle
import system.designsystem.component.TopAppBar
import system.core.lifecycle.SaveAbleState

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
        TextSubTitle(
            """
            This is detail screen for Todo with id = ${pld.id}.
            I'm too lazy to do it.
            But this is enough to demonstrate navigation.
        """.trimIndent(),
            modifier = Modifier.padding(
                horizontal = LargePadding
            )
        )
        TextHeadline1(
            "Headline 1",
            modifier = Modifier.padding(
                horizontal = LargePadding
            )
        )
        TextBody(
            "This is normal text. aksdnkj aslkd lakndl asl salkdnlak dnslkas dlak dlad landlk asd. askjdnoa dla dlaksd alsd las da.",
            modifier = Modifier.padding(
                horizontal = LargePadding
            )
        )
        TextHeadline2(
            "Headline 2",
            modifier = Modifier.padding(
                horizontal = LargePadding
            )
        )

        TextBody(
            "This is normal text. aksdnkj aslkd lakndl asl salkdnlak dnslkas dlak dlad landlk asd. askjdnoa dla dlaksd alsd las da.",
            modifier = Modifier.padding(
                horizontal = LargePadding
            )
        )
        TextHeadline3(
            "Headline 3",
            modifier = Modifier.padding(
                horizontal = LargePadding
            )
        )
        TextBody(
            "This is normal text. aksdnkj aslkd lakndl asl salkdnlak dnslkas dlak dlad landlk asd. askjdnoa dla dlaksd alsd las da.",
            modifier = Modifier.padding(
                horizontal = LargePadding
            )
        )
    }
}