package br.com.horseconnect.impl.domain.usecase.image

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.image.ImageRepository
import br.com.horseconnect.impl.domain.model.Image

class UploadImageUseCase(
    private val repository: ImageRepository
): BaseUseCase<ByteArray, Image> {
    override suspend fun invoke(params: ByteArray?): ResponseResult<Image> {
        return params?.let {
            repository.uploadImage(it)
        } ?: ResponseResult.Error(CommonErrors.UnknownError())
    }
}