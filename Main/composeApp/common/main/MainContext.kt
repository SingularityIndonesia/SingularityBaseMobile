package main

import ProjectConfig
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
                ProjectConfig.HOST,
                ProjectConfig.TODO_API_BASE_PATH
            )
        )
    }

    val aiChatContext: AIChatContext by lazy {
        AIChatContext(
            geminiApiKey = ProjectConfig.GEMINI_API_KEY
        )
    }

    val dashboardContext: DashboardContext by lazy {
        DashboardContext(
            exampleContext = exampleContext,
            aiChatContext = aiChatContext
        )
    }
}
