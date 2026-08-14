package br.com.horseconnect.impl.domain.usecase.vaccine

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.vaccine.VaccineRepository
import br.com.horseconnect.impl.domain.model.Vaccine

class UpdateVaccineUseCase(
    private val repository: VaccineRepository
): BaseUseCase<Vaccine, Unit> {
    override suspend fun invoke(params: Vaccine?): ResponseResult<Unit> {
        return params?.let {
            repository.updateVaccine(it)
        } ?: ResponseResult.Error(CommonErrors.UnknownError())
    }
}
