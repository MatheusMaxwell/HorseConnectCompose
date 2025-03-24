package br.com.horseconnect.impl.domain.usecase

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.animal.AnimalRepository
import br.com.horseconnect.impl.domain.model.Genealogy

class GetGenealogyByAnimalIdUseCase(
    private val repository: AnimalRepository
): BaseUseCase<String, Genealogy> {
    override suspend fun invoke(params: String?): ResponseResult<Genealogy> {
        return params?.let {
            repository.getAnimalGenealogyByAnimalId(it)
        } ?: ResponseResult.Error(CommonErrors.UnknownError())
    }
}