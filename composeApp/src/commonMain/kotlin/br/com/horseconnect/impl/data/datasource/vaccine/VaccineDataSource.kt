package br.com.horseconnect.impl.data.datasource.vaccine

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.model.VaccineData
import br.com.horseconnect.impl.domain.model.Vaccine

interface VaccineDataSource {

    suspend fun getVaccines(farmId: String): ResponseResult<List<Vaccine>>
    suspend fun addVaccine(vaccine: VaccineData): ResponseResult<Unit>
    suspend fun updateVaccine(vaccine: VaccineData, vaccineId: String): ResponseResult<Unit>
    suspend fun deleteVaccine(id: String): ResponseResult<Unit>
}
