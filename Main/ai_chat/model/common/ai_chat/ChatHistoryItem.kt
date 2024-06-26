package ai_chat

import common.VmProcessing
import common.VmState
import common.fold


data class ChatHistoryItem(
    val message: String,
    val answer: ChatResponse = ChatResponse(),
)

data class ChatResponse(
    val answerState: VmState<String> = VmProcessing()
) {
    val message: String
        get() = answerState.fold(
            ifSuccess = { it },
            ifFailed = { "Error: ${it.message}" }
        ) { "Loading.." }
}