/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.postman_client_generator.companion


sealed interface NumberTypeResolverStrategy {
    /**
     * Will give you either: Double, Long, BigInteger, BigDecimal, or String
     */
    fun predictedType(clues: List<String?>): String
}

object ToStringStrategy : NumberTypeResolverStrategy {
    override fun predictedType(clues: List<String?>): String = "String"
}

object SmartResolverStrategy : NumberTypeResolverStrategy {
    override fun predictedType(clues: List<String?>): String {
        return clues
            .filterNotNull()
            .fold("0") { acc, v ->
                // prefer decimal over int
                when {
                    // compare longer decimal
                    v.contains(".") && v.contains(".") ->
                        if (v.length > acc.length) v else acc

                    // select decimal over int
                    v.contains(".") && !acc.contains(".") -> v
                    !v.contains(".") && acc.contains(".") -> acc

                    // select longer int
                    v.length > acc.length -> v
                    else -> acc
                }
            }
            .let {
                // resolve decimal
                if (it.contains(".")) {
                    if (it.length < 5)
                        return@let "Float"
                    else
                        return@let "Double"
                } else {
                    if (it.length < 10)
                        return@let "Int"
                    else
                        return@let "Long"
                }
            }
    }

}