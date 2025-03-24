package br.com.horseconnect.impl.domain.usecase

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.farm.FarmRepository
import br.com.horseconnect.impl.domain.model.Farm

class GetUserFarmUseCase(
    private val repository: FarmRepository
): BaseUseCase<Unit, Farm> {
    override suspend fun invoke(params: Unit?): ResponseResult<Farm> =
        repository.getUserFarm()

}