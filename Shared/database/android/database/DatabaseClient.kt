package database

actual fun createDatabaseClient() : DatabaseClient {
    return object : DatabaseClient {
        override suspend fun <T : Any> put(entity: T): Result<DBEntity<T>> {
            TODO("Not yet implemented")
        }

        override suspend fun <T : Any> record(entity: T): Result<DBEntity<T>> {
            TODO("Not yet implemented")
        }

        override suspend fun <T : Any> first(): Result<DBEntity<T>> {
            TODO("Not yet implemented")
        }

        override suspend fun <T : Any> top(size: Int, page: Int): Result<DBEntity<T>> {
            TODO("Not yet implemented")
        }

        override suspend fun <T : Any> last(): Result<DBEntity<T>> {
            TODO("Not yet implemented")
        }

        override suspend fun <T : Any> bottom(size: Int, page: Int): Result<DBEntity<T>> {
            TODO("Not yet implemented")
        }

        override suspend fun <T : Any> delete(selector: (T) -> Boolean): Result<DBEntity<T>> {
            TODO("Not yet implemented")
        }
    }
}