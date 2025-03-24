package br.com.horseconnect.common.ext

import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.error.GenericError

fun Exception.toGenericError(): GenericError {
    return CommonErrors.UnknownError()
}
