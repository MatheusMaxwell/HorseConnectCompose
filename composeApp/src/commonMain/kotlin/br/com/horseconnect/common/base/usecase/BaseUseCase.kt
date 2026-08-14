package br.com.horseconnect.common.base.usecase

import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.result.ResponseResult

interface BaseUseCase<Param, Output> {
    suspend fun invoke(params: Param? = null): ResponseResult<Output>
}

suspend fun <P, T> executeUseCaseCallback(
    params: P?,
    expectedResult: suspend (P) -> ResponseResult<T>
): ResponseResult<T> {
    return params?.let {
        expectedResult.invoke(it)
    } ?: ResponseResult.Error(CommonErrors.UnknownError())
}
