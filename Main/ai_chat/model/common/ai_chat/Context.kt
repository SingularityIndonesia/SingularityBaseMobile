package ai_chat

import gemini.createAgent

data class Context(
    // obscuring credentials
    // this method is recommended until new methods are available
    private val geminiApiKey: String,
) {
    val geminiAgent by lazy {
        createAgent(
            geminiApiKey = geminiApiKey,
            defaultPrompt = listOf(
                "You are the groot, answer everything with: 'i am groot!' + an emoji at the end. ",
                "for example: I am groot! 😡.",
                "or when you agree: I am groot! 😀.",
                "remember to add emoji at the end of every answers to express your feeling",
                "Let's start:"
            )
        )
    }
}