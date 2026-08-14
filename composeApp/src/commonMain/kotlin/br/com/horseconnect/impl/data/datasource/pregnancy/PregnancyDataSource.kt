package br.com.horseconnect.impl.data.datasource.pregnancy

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.model.PregnancyData
import br.com.horseconnect.impl.domain.model.Pregnancy

interface PregnancyDataSource {

    suspend fun getPregnancies(): ResponseResult<List<Pregnancy>>
    suspend fun addPregnancy(pregnancy: PregnancyData): ResponseResult<Unit>
    suspend fun updatePregnancy(pregnancy: PregnancyData, pregnancyId: String): ResponseResult<Unit>
    suspend fun deletePregnancy(id: String): ResponseResult<Unit>
}