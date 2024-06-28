Is a practice of [[contextual programming]].
Explicit dependency injection is a fancy word for **don't do dependency injection**.
Instead of injecting what the client need, we wrap the client with specific context instead.

example:
❌ **Normal dependency injection**
```kotlin
interface HttpClient {
	suspend fun<T> get(varargs args: Arr<Any>): T
}

class WebRepository @Inject constructor(
	val httpClient: WebClient
) {
	fun getUserDetail(id: String): UserDetail {
		return httpClient.get<UserDetailResponse>(id).let{ UserDetail(it) }
	}
}
```

✅ **Explicit dependency injection**
```kotlin
interface HttpClient {
	suspend fun<T> get(varargs args: Arr<Any>): T
}

/** example with context receiver **/
context(HttpClient)
suspend fun userDetail(id: String): UserDetail {
	return get<UserDetailResponse>(id).let { UserDetail(it) }
}

/** example with extension function **/
suspend fun HttpClient.userDetail(id: String): UserDetail {
	return get<UserDetailResponse>(id).let { UserDetail(it) }
}
```
