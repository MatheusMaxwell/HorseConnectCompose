package br.com.horseconnect.impl.data.datasource.pregnancy

import br.com.horseconnect.common.ext.toGenericError
import br.com.horseconnect.common.manager.SessionManager
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.mapper.toDomain
import br.com.horseconnect.impl.data.model.AnimalData
import br.com.horseconnect.impl.data.model.PregnancyData
import br.com.horseconnect.impl.domain.model.Pregnancy
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.LocalCacheSettings
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.firestoreSettings
import kotlinx.serialization.builtins.serializer

private const val PREGNANCIES_COLLECTION = "pregnancies"
private const val  FARM_ID_FIELD = "farmId"

class PregnancyDataSourceImpl: PregnancyDataSource {

    private val db = Firebase.firestore

    init {
        db.settings = firestoreSettings {
            cacheSettings = LocalCacheSettings.Persistent.newBuilder().build()
        }
    }

    override suspend fun getPregnancies(): ResponseResult<List<Pregnancy>>{
        val farmId = SessionManager.farm?.id.orEmpty()
        return try {
            val snapshot = db
                .collection(PREGNANCIES_COLLECTION)
                .where { FARM_ID_FIELD equalTo farmId }
                .get()

            val pregnancies = snapshot.documents.map {
                val data = it.data(strategy = PregnancyData.serializer())
                data.toDomain(it.id)
            }
            ResponseResult.Success(pregnancies)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }

    override suspend fun addPregnancy(pregnancy: PregnancyData): ResponseResult<Unit> {
        return try {
            db.collection(PREGNANCIES_COLLECTION).add(pregnancy)
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }

    override suspend fun updatePregnancy(pregnancy: PregnancyData, pregnancyId: String): ResponseResult<Unit> {
        return try {
            db.collection(PREGNANCIES_COLLECTION).document(pregnancyId).set(pregnancy)
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }

    override suspend fun deletePregnancy(id: String): ResponseResult<Unit> {
        return try {
            db.collection(PREGNANCIES_COLLECTION).document(id).delete()
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }
}