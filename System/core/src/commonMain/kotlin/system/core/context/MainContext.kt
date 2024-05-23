/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package system.core.context

interface MainContext {
    val webRepositoryContext: WebRepositoryContext
    /*val storageContext: StorageContext*/

    companion object {
        lateinit var mainContext: MainContext
        fun init(context: MainContext) {
            mainContext = context
        }

        inline operator fun <reified T> invoke(): T =
            when (T::class.qualifiedName) {
                WebRepositoryContext::class.qualifiedName -> mainContext.webRepositoryContext as T
                /*StorageContext::class.qualifiedName -> mainContext.storageContext as T*/
                else -> throw IllegalArgumentException("Unknown context ${T::class.simpleName}")
            }
    }
}

