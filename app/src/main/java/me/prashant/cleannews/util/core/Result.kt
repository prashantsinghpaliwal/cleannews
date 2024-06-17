package me.prashant.cleannews.util.core

sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()

    data class Success<out T>(
        val data: T,
    ) : Resource<T>()

    data class Error(
        val exception: Throwable,
    ) : Resource<Nothing>()

    companion object {
        fun <T> success(data: T): Resource<T> = Success(data)

        fun error(exception: Throwable): Resource<Nothing> = Error(exception)
    }
}
