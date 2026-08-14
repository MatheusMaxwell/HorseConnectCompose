package br.com.horseconnect.impl.data.repository.pregnancy

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.model.PregnancyData
import br.com.horseconnect.impl.domain.model.Pregnancy

interface PregnancyRepository {

    suspend fun getPregnancies(): ResponseResult<List<Pregnancy>>
    suspend fun addPregnancy(pregnancy: Pregnancy): ResponseResult<Unit>
    suspend fun updatePregnancy(pregnancy: Pregnancy): ResponseResult<Unit>
    suspend fun deletePregnancy(id: String): ResponseResult<Unit>
}