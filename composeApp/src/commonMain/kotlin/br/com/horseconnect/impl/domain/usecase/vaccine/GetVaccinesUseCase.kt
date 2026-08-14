package br.com.horseconnect.impl.domain.usecase.vaccine

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.vaccine.VaccineRepository
import br.com.horseconnect.impl.domain.model.Vaccine

class GetVaccinesUseCase(
    private val repository: VaccineRepository
): BaseUseCase<Unit, List<Vaccine>> {
    override suspend fun invoke(params: Unit?): ResponseResult<List<Vaccine>> {
        return repository.getVaccines()
    }
}
