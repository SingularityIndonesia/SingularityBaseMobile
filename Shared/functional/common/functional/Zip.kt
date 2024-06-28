package functional

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

/** Sequential Call Example **/
suspend fun fetchAllUsersSequential() = withContext(Dispatchers.Default) {
    val userIDs = listOf("1","2","3")
    userIDs.map {
        getUserDetail(it)
    }
}

/** Parallel Call Example **/
suspend fun fetchAllUsersParallel() = withContext(Dispatchers.Default) {
    val userIDs = listOf("1","2","3")

    // jobs
    userIDs
        .map { id ->
            async {
                getUserDetail(id)
            }
        }
        .awaitAll()
}

/** Companion Block **/
data class User(val id: String)

suspend fun getUserDetail(
    id: String
): Result<User> = withContext(Dispatchers.Default) {
    TODO()
}