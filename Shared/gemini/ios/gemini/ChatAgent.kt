package gemini

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


actual fun createAgent(
    geminiApiKey: String,
    modelName: String,
    defaultPrompt: List<String>
): GeminiAgent {

    return object : GeminiAgent {
        override suspend fun sendMessage(
            message: String
        ): Result<String> =
            withContext(Dispatchers.Default) {
                runCatching {

                    throw Error("Ios is not yet supported")
                }
            }
    }
}
