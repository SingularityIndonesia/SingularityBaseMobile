package gemini

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiClient(
    private val geminiApiKey: String,
    private val modelName: String = "gemini-1.5-flash",
    private val generationConfig: GenerationConfig =
        generationConfig {
            temperature = 1f
            topK = 64
            topP = 0.95f
            maxOutputTokens = 8192
            responseMimeType = "text/plain"
        },
    private val defaultPrompt: List<String>,
    private val safetySettings: List<SafetySetting> =
        listOf(
            SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
            SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
            SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
            SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE),
        )
) {

    private val model = GenerativeModel(
        modelName,
        // Retrieve API key as an environmental variable defined in a Build Configuration
        // see https://github.com/google/secrets-gradle-plugin for further instructions
        geminiApiKey,
        generationConfig = generationConfig,
        safetySettings = safetySettings,
    )

    val chat by lazy {
        model.startChat(
            listOf(
                defaultPrompt.let {
                    content("user") {
                        it.forEach {
                            text(it)
                        }
                    }
                }
            )
        )
    }
}

actual fun createAgent(
    geminiApiKey: String,
    modelName: String,
    defaultPrompt: List<String>
): GeminiAgent {
    val geminiClient = GeminiClient(
        geminiApiKey = geminiApiKey,
        modelName = modelName,
        defaultPrompt = defaultPrompt
    )

    return object : GeminiAgent {
        override suspend fun sendMessage(
            message: String
        ): Result<String> = withContext(Dispatchers.Default) {
            runCatching {
                val response = geminiClient.chat.sendMessage(message)

                if (response.promptFeedback?.blockReason != null) {
                    throw Throwable(
                        response.promptFeedback?.blockReason?.name
                    )
                }

                response.text ?: throw NullPointerException("Response null")
            }
        }
    }
}

