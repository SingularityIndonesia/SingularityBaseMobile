package database

interface DBEntity<T : Any> {
    val id: String
    val content: T
}

data class Record<T : Any>(
    override val id: String,
    override val content: T,
    val histories: Sequence<T>,
) : DBEntity<T>

interface DatabaseClient {

    /**
     * Inserts a new entity into the database.
     */
    suspend fun <T : Any> put(entity: T): Result<DBEntity<T>>

    /**
     * Similar to Patch but safer.
     * Record will replace the old entity with the new one in the table, but the old entity will not be removed from the database.
     * The old entity will be pushed back to the entity's record history.
     * Example:
     * ```
     * before, you have: [En1, En2, En3]
     * then you run : record(En2Modified)
     * after that you will have: [En1, Record(En2), En3]
     * ```
     */
    suspend fun <T : Any> record(entity: T): Result<DBEntity<T>>

    /**
     * Get first entity from the database.
     */
    suspend fun <T : Any> first(): Result<DBEntity<T>>

    /**
     * Get first entities from the database.
     */
    suspend fun <T : Any> top(size: Int, page: Int = 0): Result<DBEntity<T>>

    /**
     * Get last entity from the database.
     */
    suspend fun <T : Any> last(): Result<DBEntity<T>>

    /**
     * Get last entities from the database.
     */
    suspend fun <T : Any> bottom(size: Int, page: Int = 0): Result<DBEntity<T>>

    /**
     * Delete entity from database.
     */
    suspend fun <T : Any> delete(selector: (T) -> Boolean): Result<DBEntity<T>>
}

expect fun createDatabaseClient() : DatabaseClient