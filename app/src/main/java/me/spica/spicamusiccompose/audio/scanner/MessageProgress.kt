package me.spica.spicamusiccompose.audio.scanner

sealed class FlowEvent<out T, out U> {
    class Progress<T, U>(val data: U) : FlowEvent<T, U>()
    class Success<T>(val result: T) : FlowEvent<T, Nothing>()
    class Failure(val message: String?) : FlowEvent<Nothing, Nothing>()
}

data class MessageProgress(
    val message: String,
    val progress: Progress?

) {
    override fun toString(): String {
        return "MessageProgress(message='$message', progress=$progress)"
    }
}

data class Progress(val progress: Int, val total: Int) {
    fun asFloat(): Float {
        return progress / total.toFloat()
    }
}
