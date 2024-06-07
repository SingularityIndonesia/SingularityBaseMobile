/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.postman_client_generator

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Postman(

    @SerialName("item")
    val item: List<ItemItem?>? = null,

    @SerialName("info")
    val info: Info? = null,

    @SerialName("variable")
    val variable: List<VariableItem?>? = null,

    @SerialName("event")
    val event: List<EventItem?>? = null,

    @SerialName("request")
    val request: Request? = null,

    @SerialName("response")
    val response: List<ResponseItem?>? = null,

    @SerialName("name")
    val name: String? = null,

    ) {
    @Serializable
    data class EventItem(

        @SerialName("listen")
        val listen: String? = null,

        @SerialName("script")
        val script: Script? = null
    )

    @Serializable
    data class Script(

        @SerialName("type")
        val type: String? = null,

        @SerialName("exec")
        val exec: List<String?>? = null
    )

    @Serializable
    data class VariableItem(

        @SerialName("value")
        val value: String? = null,

        @SerialName("key")
        val key: String? = null,

        @SerialName("type")
        val type: String? = null,
    )

    @Serializable
    data class BearerItem(

        @SerialName("type")
        val type: String? = null,

        @SerialName("value")
        val value: String? = null,

        @SerialName("key")
        val key: String? = null
    )

    @Serializable
    data class Request(

        @SerialName("method")
        val method: String? = null,

        @SerialName("header")
        val header: List<HeaderItem?>? = null,

        @SerialName("url")
        val url: Url? = null,

        @SerialName("auth")
        val auth: Auth? = null,

        @SerialName("body")
        val body: Body? = null,
    )

    @Serializable
    data class HeaderItem(

        @SerialName("type")
        val type: String? = null,

        @SerialName("value")
        val value: String? = null,

        @SerialName("key")
        val key: String? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("description")
        val description: String? = null
    )

    @Serializable
    data class QueryItem(

        @SerialName("value")
        val value: String? = null,

        @SerialName("key")
        val key: String? = null
    )

    @Serializable
    data class Auth(

        @SerialName("bearer")
        val bearer: List<BearerItem?>? = null,

        @SerialName("type")
        val type: String? = null
    )

    @Serializable
    data class ItemItem(

        @SerialName("request")
        val request: Request? = null,

        @SerialName("response")
        val response: List<ResponseItem?>? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("item")
        val item: List<ItemItem?>? = null
    )

    @Serializable
    data class ResponseItem(

        @SerialName("originalRequest")
        val originalRequest: OriginalRequest? = null,

        @SerialName("_postman_previewlanguage")
        val postmanPreviewlanguage: String? = null,

        @SerialName("cookie")
        val cookie: List<JsonObject?>? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("header")
        val header: List<HeaderItem?>? = null,

        @SerialName("body")
        val body: String? = null,

        // todo: not yet supported.
        /*@SerialName("cookie")
        val cookie: List<Any?>? = null,*/

        @SerialName("code")
        val code: Int? = null,

        @SerialName("status")
        val status: String? = null
    )

    @Serializable
    data class Url(

        @SerialName("path")
        val path: List<String?>? = null,

        @SerialName("host")
        val host: List<String?>? = null,

        @SerialName("raw")
        val raw: String? = null,

        @SerialName("variable")
        val variable: List<VariableItem?>? = null,

        @SerialName("query")
        val query: List<QueryItem?>? = null
    )

    @Serializable
    data class Body(

        @SerialName("mode")
        val mode: String? = null,

        @SerialName("options")
        val options: Options? = null,

        @SerialName("raw")
        val raw: String? = null
    )

    @Serializable
    data class Options(

        @SerialName("raw")
        val raw: Raw? = null
    )

    @Serializable
    data class OriginalRequest(

        @SerialName("method")
        val method: String? = null,

        @SerialName("header")
        val header: List<HeaderItem?>? = null,

        @SerialName("body")
        val body: Body? = null,

        @SerialName("url")
        val url: Url? = null
    )

    @Serializable
    data class Raw(

        @SerialName("language")
        val language: String? = null
    )

    @Serializable
    data class Info(

        @SerialName("schema")
        val schema: String? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("_exporter_id")
        val exporterId: String? = null,

        @SerialName("_postman_id")
        val postmanId: String? = null
    )
}
