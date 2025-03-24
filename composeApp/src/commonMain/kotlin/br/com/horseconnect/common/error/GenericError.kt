package br.com.horseconnect.common.error

abstract class GenericError : Exception() {
    open val msg: String = "Ocorreu um erro. Tente novamente."
}