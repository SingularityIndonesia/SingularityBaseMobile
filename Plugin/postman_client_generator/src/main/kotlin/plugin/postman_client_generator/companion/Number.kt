/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 17/05/2024 14:05
 * You are not allowed to remove the copyright.
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
                    if (it.length < 10)
                        return@let "Double"
                    else return@let "BigDecimal"
                } else {
                    if (it.length < 10)
                        return@let "Int"
                    if (it.length < 19)
                        return@let "Long"
                    else return@let "BigInteger"
                }
            }
    }

}