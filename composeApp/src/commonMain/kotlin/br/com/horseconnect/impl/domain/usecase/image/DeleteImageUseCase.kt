package br.com.horseconnect.impl.domain.usecase.image

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.image.ImageRepository

class DeleteImageUseCase(
    private val imageRepository: ImageRepository
): BaseUseCase<String, Unit> {
    override suspend fun invoke(params: String?): ResponseResult<Unit> {
        return imageRepository.deleteImage(params.orEmpty())
    }
}