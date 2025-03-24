package br.com.horseconnect.impl.data.datasource.image

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Image

interface ImageDataSource {

    suspend fun uploadImage(image: ByteArray): ResponseResult<Image>
}