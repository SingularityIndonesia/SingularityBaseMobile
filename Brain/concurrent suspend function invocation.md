# sequential suspend function invocation
```kotlin
// Note: If you have dispatcher IO, better use IO instead
suspend fun fetchAllUsersSequential() = withContext(Dispatchers.Default) {
    val userIDs = listOf("1","2","3")
    userIDs.map {
        getUserDetail(it)
    }
}
```
# concurrent suspend function invocation
```kotlin
// Note: If you have dispatcher IO, better use IO instead
suspend fun fetchAllUsersParallel() = withContext(Dispatchers.Default) {
    val userIDs = listOf("1","2","3")

    userIDs
        .map { id ->
            async {
                getUserDetail(id)
            }
        }
        .awaitAll()
}

data class User(val id: String)

suspend fun getUserDetail(
    id: String
): Result<User> = withContext(Dispatchers.Default) {
    TODO()
}
```

