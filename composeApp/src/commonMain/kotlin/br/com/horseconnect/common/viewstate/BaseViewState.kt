package br.com.horseconnect.common.viewstate

sealed class BaseViewState<out R>() {
    data object Initial: BaseViewState<Nothing>()
    data object Loading : BaseViewState<Nothing>()
    data class Error(val msg: String) : BaseViewState<Nothing>()
    data class Populated<out T>(val data: T) : BaseViewState<T>()
}
