package gemini

interface GeminiAgent {
    suspend fun sendMessage(
        message: String
    ): Result<String>
}

expect fun createAgent(
    geminiApiKey: String,
    modelName: String = "gemini-1.5-flash",
    defaultPrompt: List<String> = listOf(
        "You are a multipurpose ai assistance.",
        "Answer anything user asks."
    )
): GeminiAgent
