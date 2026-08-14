package br.com.horseconnect.impl.domain.usecase.animal

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.animal.AnimalRepository
import br.com.horseconnect.impl.domain.model.Genealogy

class UpdateGenealogyUseCase(
    private val repository: AnimalRepository
): BaseUseCase<Genealogy, Unit> {
    override suspend fun invoke(params: Genealogy?): ResponseResult<Unit> {
        return params?.let {
            repository.updateGenealogy(it)
        } ?: ResponseResult.Error(CommonErrors.UnknownError())
    }
}
