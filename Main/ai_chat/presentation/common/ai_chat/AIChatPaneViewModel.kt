package ai_chat

import ai_chat.entity.ChatHistoryItemDisplay
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import common.VmFailed
import common.VmSuccess
import common.moveToDefault
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

sealed class Intent {
    data class Chat(
        val message: String
    ) : Intent()
}

class AIChatPaneState(saveAble: SaveAble) {
    internal val chatHistories = MutableStateFlow(saveAble.chatHistories)

    data class SaveAble(
        val chatHistories: List<ChatHistoryItemDisplay> = listOf()
    )

    fun saveAble() = SaveAble(
        chatHistories = chatHistories.value
    )
}

class AIChatPaneStateReducer(
    state: AIChatPaneState
) {
    val historyDisplayItems = combine(
        flowOf { Unit },
        state.chatHistories
    ) { _, chatHistories ->
        chatHistories
    }.moveToDefault()
}

class AIChatPaneViewModel(
    private val context: Context,
    defaultSate: AIChatPaneState.SaveAble,
) : ViewModel() {

    val state = AIChatPaneState(defaultSate)
    val reducer = AIChatPaneStateReducer(state)

    fun Post(intent: Intent) {
        when (intent) {
            is Intent.Chat -> submitChat(ChatHistoryItem(intent.message))
        }
    }

    private fun submitChat(
        chat: ChatHistoryItem
    ) = viewModelScope.launch {
        val entity = ChatHistoryItemDisplay(chat)

        // add to history list
        run {
            val newHistories = state.chatHistories.first().plus(entity)
            state.chatHistories.emit(newHistories)
        }

        // submit to gemini
        val response = context.geminiAgent.sendMessage(chat.message)
            .fold(
                onSuccess = {
                    VmSuccess(it)
                },
                onFailure = {
                    VmFailed(Exception(it))
                }
            )

        // new entity
        val newEntity = entity.copy(
            chatHistoryItem = entity.chatHistoryItem.copy(
                answer = entity.chatHistoryItem.answer.copy(
                    answerState = response
                )
            )
        )

        // new list
        val newList = state.chatHistories.first()
            .toMutableList()
            .apply {
                val index = state.chatHistories.first().indexOf(entity)
                set(index, newEntity)
            }
            .toList()

        // emit new list
        state.chatHistories.emit(newList)
    }
}