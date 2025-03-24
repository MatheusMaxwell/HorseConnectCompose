package br.com.horseconnect.impl.data.repository.farm

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Farm

interface FarmRepository {

    suspend fun getUserFarm(): ResponseResult<Farm>
}