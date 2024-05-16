package plugin.postman_client_generator.companion


sealed interface NumberTypeResolverStrategy {
    /**
     * Will give you either: Double, Long, BigInteger, BigDecimal, or String
     */
    fun predictedType(clue: String?): String
}

object ToStringStrategy : NumberTypeResolverStrategy {
    override fun predictedType(clue: String?): String = "String"
}

object SmartResolverStrategy : NumberTypeResolverStrategy {
    override fun predictedType(clue: String?): String {
        throw NotImplementedError("${this::class.simpleName} is not yet supported.")
    }

}