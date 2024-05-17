/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 17/05/2024 14:05
 * You are not allowed to remove the copyright.
 */
package plugin.postman_client_generator

import plugin.postman_client_generator.companion.SmartResolverStrategy
import java.util.regex.Pattern

sealed interface ClientGeneratorStrategy {
    fun generateClient(
        contexts: List<Context>,
        name: String,
        nameSpace: String,
        groupName: String,
        request: Postman.Request,
        response: List<Postman.ResponseItem>
    ): PostmanClient
}

object CommonClientGenerator : ClientGeneratorStrategy {
    override fun generateClient(
        contexts: List<Context>,
        name: String,
        nameSpace: String,
        groupName: String,
        request: Postman.Request,
        response: List<Postman.ResponseItem>
    ): PostmanClient {

        /**
         * ```
         * "path": [
         * 	"books",
         * 	":bookID",
         * 	"comments"
         * ]
         * Into : books/${bookID}/comments
         */
        val endpoint = request.url?.path
            ?.filterNotNull()
            ?.map {
                if (it.contains(":")) {
                    val pureName = it.replaceFirstChar { "" }
                    "\${$pureName}"
                } else {
                    it
                }
            }
            ?.fold("") { acc, v -> "$acc/$v" }
            ?: throw IllegalArgumentException("path is null:\n $request")

        val pathArguments = run {
            val pattern = Pattern.compile("""\{(.*?)}""")
            val matcher = pattern.matcher(endpoint)
            val results = mutableListOf<String>()

            while (matcher.find()) {
                results.add(matcher.group(1))
            }

            results
        }

        val method = request.method
            ?: throw NullPointerException("method is null:\n$request")

        val requestModelName = "${name}Request"
        val requestModel =
            RequestModel(
                name = requestModelName,
                request = request
            )

        val responseModelName = "${name}Response"
        val responseModel =
            ResponseModel(
                name = responseModelName,
                response = response
            )

        val headers = request.header?.filterNotNull() ?: listOf()
        val headerModelName = "${name}Header"

        val headerModel =
            Header(
                name = headerModelName,
                headers = headers
            )

        val content = clientTemplate(
            functionName = name,
            method = method,
            headerModelName = headerModelName,
            headerModel = headerModel,
            requestModelName = requestModelName,
            requestModelNumberTypeResolverStrategy = SmartResolverStrategy,
            requestModel = requestModel,
            responseModelName = responseModelName,
            responseModelNumberTypeResolverStrategy = SmartResolverStrategy,
            responseModel = responseModel,
            pathArguments = pathArguments,
            endpoint = endpoint
        )

        return PostmanClient(
            name = name,
            nameSpace = nameSpace,
            groupName = groupName,
            content = content
        )
    }
}

object HEADClientGenerator : ClientGeneratorStrategy {
    override fun generateClient(
        contexts: List<Context>,
        name: String,
        nameSpace: String,
        groupName: String,
        request: Postman.Request,
        response: List<Postman.ResponseItem>
    ): PostmanClient {
        // fixme
        return PostmanClient(
            name = "Dummy$name",
            nameSpace = nameSpace,
            groupName = groupName,
            content = "// Head is not yet supported"
        )
    }
}

object OPTIONSClientGenerator : ClientGeneratorStrategy {
    override fun generateClient(
        contexts: List<Context>,
        name: String,
        nameSpace: String,
        groupName: String,
        request: Postman.Request,
        response: List<Postman.ResponseItem>
    ): PostmanClient {
        // fixme
        return PostmanClient(
            name = "Dummy$name",
            nameSpace = nameSpace,
            groupName = groupName,
            content = "// Options is not yet supported"
        )
    }
}
