package ai_chat.entity

import ai_chat.ChatHistoryItem

data class ChatHistoryItemDisplay(
    val chatHistoryItem: ChatHistoryItem
) {
    val message get() = chatHistoryItem.message
    val answer get() = chatHistoryItem.answer.message
}