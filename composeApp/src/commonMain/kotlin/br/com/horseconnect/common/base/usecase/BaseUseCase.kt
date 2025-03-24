package br.com.horseconnect.common.base.usecase

import br.com.horseconnect.common.result.ResponseResult

interface BaseUseCase<Param, Output> {
    suspend fun invoke(params: Param? = null): ResponseResult<Output>?
}
