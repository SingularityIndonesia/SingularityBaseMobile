package ai_chat

import arrow.optics.optics
import common.VmProcessing
import common.VmState

@optics
data class ChatHistoryItem(
    val message: String,
    val answer: ChatResponse = ChatResponse(),
) {
    companion object
}

@optics
data class ChatResponse(
    val answerState: VmState<String> = VmProcessing(),
) {
    companion object
}
