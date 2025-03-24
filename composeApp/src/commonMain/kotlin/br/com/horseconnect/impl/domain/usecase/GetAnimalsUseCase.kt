package br.com.horseconnect.impl.domain.usecase

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.animal.AnimalRepository
import br.com.horseconnect.impl.domain.model.Animal

class GetAnimalsUseCase(
    private val repository: AnimalRepository
): BaseUseCase<Unit, List<Animal>> {
    override suspend fun invoke(params: Unit?): ResponseResult<List<Animal>> = repository.getAnimals()
}