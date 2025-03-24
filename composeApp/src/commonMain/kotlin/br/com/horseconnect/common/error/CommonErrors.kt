package br.com.horseconnect.common.error

object CommonErrors {
    class UnknownError : GenericError()
    class NotFound(override val msg: String = "Não encontrado.") : GenericError()
}