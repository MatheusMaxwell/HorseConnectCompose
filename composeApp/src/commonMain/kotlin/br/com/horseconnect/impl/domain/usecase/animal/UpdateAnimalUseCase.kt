package br.com.horseconnect.impl.domain.usecase.animal

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.animal.AnimalRepository
import br.com.horseconnect.impl.domain.model.Animal

class UpdateAnimalUseCase(
    private val repository: AnimalRepository
): BaseUseCase<Animal, Unit> {
    override suspend fun invoke(params: Animal?): ResponseResult<Unit> {
        return params?.let {
            repository.updateAnimal(it)
        } ?: ResponseResult.Error(CommonErrors.UnknownError())
    }
}