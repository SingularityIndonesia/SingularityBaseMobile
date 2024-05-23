/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package system.core.lifecycle

class SaveAbleState {

    @Deprecated("Do not use this directly!")
    val state = mutableListOf<Any>()

    @Suppress("DEPRECATION")
    fun <T : Any> push(state: T) {
        this.state.add(state)
    }

    @Suppress("DEPRECATION")
    inline fun <reified T : Any> pop(): T? {
        val state = this.state.firstOrNull {
            it is T
        } as T?
        
        if (state != null)
            this.state.remove(state)

        return state
    }

}