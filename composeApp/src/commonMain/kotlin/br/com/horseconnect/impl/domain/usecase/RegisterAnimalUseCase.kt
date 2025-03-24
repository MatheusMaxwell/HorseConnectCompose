package br.com.horseconnect.impl.domain.usecase

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.animal.AnimalRepository
import br.com.horseconnect.impl.data.repository.animal.AnimalRepositoryImpl
import br.com.horseconnect.impl.domain.model.Animal

class RegisterAnimalUseCase(
    private val repository: AnimalRepository
): BaseUseCase<Animal, Unit> {
    override suspend fun invoke(params: Animal?): ResponseResult<Unit> {
        return params?.let {
            repository.registerAnimal(it)
        } ?: ResponseResult.Error(CommonErrors.UnknownError())
    }
}