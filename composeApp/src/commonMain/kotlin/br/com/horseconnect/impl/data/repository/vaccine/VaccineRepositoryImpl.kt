package br.com.horseconnect.impl.data.repository.vaccine

import br.com.horseconnect.common.manager.SessionManager
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.datasource.vaccine.VaccineDataSource
import br.com.horseconnect.impl.domain.mapper.toData
import br.com.horseconnect.impl.domain.model.Vaccine

class VaccineRepositoryImpl(
    private val dataSource: VaccineDataSource
): VaccineRepository {
    override suspend fun getVaccines(): ResponseResult<List<Vaccine>> {
        return dataSource.getVaccines(SessionManager.farm?.id.orEmpty())
    }

    override suspend fun addVaccine(vaccine: Vaccine): ResponseResult<Unit> {
        return dataSource.addVaccine(vaccine.toData())
    }

    override suspend fun updateVaccine(vaccine: Vaccine): ResponseResult<Unit> {
        return dataSource.updateVaccine(vaccine.toData(), vaccine.id)
    }

    override suspend fun deleteVaccine(id: String): ResponseResult<Unit> {
        return dataSource.deleteVaccine(id)
    }
}
