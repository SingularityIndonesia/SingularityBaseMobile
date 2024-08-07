package ai_chat.pane.aichat

import ai_chat.ChatHistoryItem
import ai_chat.ChatResponse
import ai_chat.Context
import ai_chat.entity.ChatHistoryItemDisplay
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import common.VmFailed
import common.VmSuccess
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

data class AIChatPaneState(
    val chatHistories: List<ChatHistoryItemDisplay> = listOf(),
)

sealed class AIChatPaneSideEffect {
    data class ShowToast(
        val message: String,
    ) : AIChatPaneSideEffect()
}

context(Context)
class AIChatPaneViewModel(
    defaultSate: AIChatPaneState,
) : ViewModel(),
    ContainerHost<AIChatPaneState, AIChatPaneSideEffect> {
    override val container: Container<AIChatPaneState, AIChatPaneSideEffect> =
        viewModelScope.container(defaultSate)

    fun chat(message: String) =
        intent {
            // submit to gemini
            val response =
                with(geminiAgent) {
                    sendMessage(message)
                        .fold(
                            onSuccess = {
                                VmSuccess(it)
                            },
                            onFailure = {
                                postSideEffect(
                                    AIChatPaneSideEffect.ShowToast(
                                        it.cause?.message ?: it.message ?: "Unknown Error",
                                    ),
                                )
                                VmFailed(Exception(it))
                            },
                        )
                }

            // new entity
            // val messageLens = ChatHistoryItemDisplay.Companion.
            val responseEntity =
                ChatHistoryItemDisplay(
                    ChatHistoryItem(
                        message = message,
                        answer = ChatResponse(response),
                    ),
                )

            // add new entity to list of the state
            reduce {
                state.copy(
                    chatHistories = state.chatHistories.plus(responseEntity),
                )
            }
        }
}
