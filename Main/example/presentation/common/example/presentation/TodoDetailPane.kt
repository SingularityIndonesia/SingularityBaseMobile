/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package example.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import core.lifecycle.StateSaver
import designsystem.LargePadding
import designsystem.component.TextBody
import designsystem.component.TextHeadline1
import designsystem.component.TextHeadline2
import designsystem.component.TextHeadline3
import designsystem.component.TextSubTitle
import designsystem.component.TopAppBar
import example.model.Context

@Immutable
data class TodoDetailPanePld(
    val id: String,
    val onBack: () -> Unit
)

@Composable
fun Context.TodoDetailPane(
    pld: TodoDetailPanePld,
    stateSaver: StateSaver
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