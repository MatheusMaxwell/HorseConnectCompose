package br.com.horseconnect.impl.data.datasource.farm

import br.com.horseconnect.common.ext.toGenericError
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.mapper.toDomain
import br.com.horseconnect.impl.data.model.FarmData
import br.com.horseconnect.impl.domain.model.Farm
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.LocalCacheSettings
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.firestoreSettings

private const val FARM_COLLECTION = "farms"
private const val  PROPRIETARY_ID_FIELD = "proprietaryId"

class FarmDataSourceImpl: FarmDataSource {
    private val db = Firebase.firestore

    init {
        db.settings = firestoreSettings {
            cacheSettings = LocalCacheSettings.Persistent.newBuilder().build()
        }
    }

    override suspend fun getFarmByProprietaryId(proprietaryId: String): ResponseResult<Farm> {
        return try {
            val snapshot = db
                .collection(FARM_COLLECTION)
                .where { PROPRIETARY_ID_FIELD equalTo proprietaryId }
                .get()

            val farm = snapshot.documents.map {
                val data = it.data(strategy = FarmData.serializer())
                data.copy(id = it.id)
            }
            ResponseResult.Success(farm.first().toDomain())
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }
}