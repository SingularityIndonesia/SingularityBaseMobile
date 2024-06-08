package main

import main.modules.ExampleWebRepositoryContext
import ai_chat.Context as AIChatContext
import dashboard.Context as DashboardContext
import example.model.Context as ExampleContext

// Centralized context control
class MainContext {
    // ExampleModule's Context
    val exampleContext: ExampleContext by lazy {
        ExampleContext(
            webRepositoryContext = ExampleWebRepositoryContext(
                EnvironmentVariables.webHost,
                EnvironmentVariables.todoApiBasePath
            )
        )
    }

    val aiChatContext: AIChatContext by lazy {
        AIChatContext(
            geminiApiKey = EnvironmentVariables.geminiApiKey
        )
    }

    val dashboardContext: DashboardContext by lazy {
        DashboardContext(
            exampleContext = exampleContext,
            aiChatContext = aiChatContext
        )
    }
}
