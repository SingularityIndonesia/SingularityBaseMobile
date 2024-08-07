package ai_chat.entity

import ai_chat.ChatHistoryItem
import arrow.optics.optics
import common.fold

@optics
data class ChatHistoryItemDisplay(
    val chatHistoryItem: ChatHistoryItem,
) {
    companion object

    val message: String = chatHistoryItem.message
    val answer: String =
        chatHistoryItem.answer.answerState.fold(
            ifIdle = { "" },
            ifProcessing = { "Processing.." },
            ifSuccess = { it },
            ifFailed = { "Error: ${it.message}" },
        )
}
