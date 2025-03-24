package br.com.horseconnect.impl.data.repository.image

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.datasource.image.ImageDataSource
import br.com.horseconnect.impl.domain.model.Image

class ImageRepositoryImpl(
    private val dataSource: ImageDataSource
): ImageRepository {
    override suspend fun uploadImage(image: ByteArray): ResponseResult<Image> {
        return dataSource.uploadImage(image)
    }
}