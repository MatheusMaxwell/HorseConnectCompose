package br.com.horseconnect.impl.data.datasource.image

import br.com.horseconnect.common.ext.toData
import br.com.horseconnect.common.ext.toGenericError
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Image
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.Data
import dev.gitlive.firebase.storage.File
import dev.gitlive.firebase.storage.FirebaseStorage
import dev.gitlive.firebase.storage.storage
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class ImageDataSourceImpl : ImageDataSource {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun uploadImage(image: ByteArray): ResponseResult<Image> {
        return try {
            val imageId = Uuid.random().toString()
            val ref = Firebase.storage.reference.child("images/${imageId}.png")
            ref.putData(data = image.toData())
            val url = ref.getDownloadUrl()
            ResponseResult.Success(Image(id = imageId, url = url))
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }
}