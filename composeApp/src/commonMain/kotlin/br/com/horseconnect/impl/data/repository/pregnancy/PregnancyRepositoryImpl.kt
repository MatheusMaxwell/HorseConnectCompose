package br.com.horseconnect.impl.data.repository.pregnancy

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.datasource.pregnancy.PregnancyDataSource
import br.com.horseconnect.impl.data.model.PregnancyData
import br.com.horseconnect.impl.domain.mapper.toData
import br.com.horseconnect.impl.domain.model.Pregnancy

class PregnancyRepositoryImpl(
    private val dataSource: PregnancyDataSource
): PregnancyRepository {
    override suspend fun getPregnancies(): ResponseResult<List<Pregnancy>> {
        return dataSource.getPregnancies()
    }

    override suspend fun addPregnancy(pregnancy: Pregnancy): ResponseResult<Unit> {
        return dataSource.addPregnancy(pregnancy.toData())
    }

    override suspend fun updatePregnancy(
        pregnancy: Pregnancy
    ): ResponseResult<Unit> {
        return dataSource.updatePregnancy(pregnancy.toData(), pregnancy.id)
    }

    override suspend fun deletePregnancy(id: String): ResponseResult<Unit> {
        return dataSource.deletePregnancy(id)
    }
}