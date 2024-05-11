package system.core.lifecycle

class SaveAbleState {

    @Deprecated("Do not use this directly!")
    val state = mutableListOf<Any>()

    fun <T : Any> push(state: T) {
        this.state.add(state)
    }

    inline fun <reified T : Any> pop(): T? {
        val state = this.state.firstOrNull {
            it is T
        } as T?
        
        if (state != null)
            this.state.remove(state)

        return state
    }

}