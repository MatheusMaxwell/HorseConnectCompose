package br.com.horseconnect.impl.data.datasource.farm

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Farm

interface FarmDataSource {
    suspend fun getFarmByProprietaryId(proprietaryId: String): ResponseResult<Farm>
}