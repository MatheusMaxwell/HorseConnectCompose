package br.com.horseconnect.impl.data.repository.vaccine

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Vaccine

interface VaccineRepository {

    suspend fun getVaccines(): ResponseResult<List<Vaccine>>
    suspend fun addVaccine(vaccine: Vaccine): ResponseResult<Unit>
    suspend fun updateVaccine(vaccine: Vaccine): ResponseResult<Unit>
    suspend fun deleteVaccine(id: String): ResponseResult<Unit>
}
