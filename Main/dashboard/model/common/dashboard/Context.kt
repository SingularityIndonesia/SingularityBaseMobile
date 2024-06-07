package dashboard

import ai_chat.Context as AIChatContext
import example.model.Context as ExampleContext


data class Context(
    val aiChatContext: AIChatContext,
    val exampleContext: ExampleContext
)