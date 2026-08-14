package br.com.horseconnect.common.network

import br.com.horseconnect.common.ext.toGenericError
import br.com.horseconnect.common.result.ResponseResult

suspend fun <T> executeNetworkCall(
    networkCall: suspend () -> T
): ResponseResult<T> {
    return try {
        networkCall.invoke().run {
            ResponseResult.Success(this)
        }
    } catch (e: Exception) {
        ResponseResult.Error(e.toGenericError())
    }
}
