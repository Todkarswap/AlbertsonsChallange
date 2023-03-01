package acronyms.app.utils

sealed class ApiResult<out R> {
    data class Success<out T>(val value: T) : ApiResult<T>()
    data class Error(
        val message: String?,
        val throwable: Throwable?
    ) : ApiResult<Nothing>()

    object Loading : ApiResult<Nothing>()


}
