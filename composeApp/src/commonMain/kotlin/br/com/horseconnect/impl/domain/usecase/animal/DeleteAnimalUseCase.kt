package br.com.horseconnect.impl.domain.usecase.animal

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.animal.AnimalRepository
import br.com.horseconnect.impl.data.repository.image.ImageRepository
import br.com.horseconnect.impl.domain.model.Animal

class DeleteAnimalUseCase(
    private val animalRepository: AnimalRepository,
    private val imageRepository: ImageRepository
): BaseUseCase<Animal, Unit> {
    override suspend fun invoke(params: Animal?): ResponseResult<Unit> {
        params?.let { animal ->
            return when(val result = animalRepository.deleteAnimal(animal.id)) {
                is ResponseResult.Success -> {
                    imageRepository.deleteImage(animal.imageId).let {
                        ResponseResult.Success(Unit)
                    }
                }
                is ResponseResult.Error -> {
                    result
                }
            }
        } ?: return ResponseResult.Error(CommonErrors.NotFound())
    }
}