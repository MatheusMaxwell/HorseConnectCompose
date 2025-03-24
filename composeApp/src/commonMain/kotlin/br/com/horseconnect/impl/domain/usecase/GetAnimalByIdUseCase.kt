package br.com.horseconnect.impl.domain.usecase

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.animal.AnimalRepository
import br.com.horseconnect.impl.domain.model.Animal

class GetAnimalByIdUseCase(
    private val repository: AnimalRepository
): BaseUseCase<String, Animal> {
    override suspend fun invoke(params: String?): ResponseResult<Animal> {
        return params?.let {
            repository.getAnimalById(it)
        } ?: ResponseResult.Error(CommonErrors.UnknownError())
    }
}