package plugin.convention.postmanclientgenerator

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Postman(

	@SerialName("item")
	val item: List<ItemItem?>? = null,

	@SerialName("info")
	val info: Info? = null
) {

	@Serializable
	data class VariableItem(

		@SerialName("value")
		val value: String? = null,

		@SerialName("key")
		val key: String? = null
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
	data class Request(

		@SerialName("method")
		val method: String? = null,

		@SerialName("header")
		val header: List<JsonElement?>? = null,

		@SerialName("url")
		val url: Url? = null,

		@SerialName("auth")
		val auth: Auth? = null
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
		val response: List<JsonElement?>? = null,

		@SerialName("name")
		val name: String? = null,

		@SerialName("item")
		val item: List<ItemItem?>? = null
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
