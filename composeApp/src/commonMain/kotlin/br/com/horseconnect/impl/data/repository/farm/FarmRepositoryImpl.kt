package br.com.horseconnect.impl.data.repository.farm

import br.com.horseconnect.common.manager.SessionManager
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.datasource.farm.FarmDataSource
import br.com.horseconnect.impl.domain.model.Farm

class FarmRepositoryImpl(
    private val datasource: FarmDataSource
): FarmRepository {
    override suspend fun getUserFarm(): ResponseResult<Farm> =
        datasource.getFarmByProprietaryId(SessionManager.user?.id.orEmpty())
}