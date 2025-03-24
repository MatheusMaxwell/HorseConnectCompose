package br.com.horseconnect.impl.data.repository.image

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Image

interface ImageRepository {

    suspend fun uploadImage(image: ByteArray): ResponseResult<Image>
}