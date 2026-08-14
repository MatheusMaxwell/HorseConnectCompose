package br.com.horseconnect.impl.data.datasource.vaccine

import br.com.horseconnect.common.ext.toGenericError
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.mapper.toDomain
import br.com.horseconnect.impl.data.model.VaccineData
import br.com.horseconnect.impl.domain.model.Vaccine
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.LocalCacheSettings
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.firestoreSettings

private const val VACCINES_COLLECTION = "vaccines"
private const val FARM_ID_FIELD = "farmId"

class VaccineDataSourceImpl: VaccineDataSource {

    private val db = Firebase.firestore

    init {
        db.settings = firestoreSettings {
            cacheSettings = LocalCacheSettings.Persistent.newBuilder().build()
        }
    }

    override suspend fun getVaccines(farmId: String): ResponseResult<List<Vaccine>> {
        return try {
            val snapshot = db
                .collection(VACCINES_COLLECTION)
                .where { FARM_ID_FIELD equalTo farmId }
                .get()

            val vaccines = snapshot.documents.map {
                val data = it.data(strategy = VaccineData.serializer())
                data.toDomain(it.id)
            }
            ResponseResult.Success(vaccines)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }

    override suspend fun addVaccine(vaccine: VaccineData): ResponseResult<Unit> {
        return try {
            db.collection(VACCINES_COLLECTION).add(vaccine)
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }

    override suspend fun updateVaccine(vaccine: VaccineData, vaccineId: String): ResponseResult<Unit> {
        return try {
            db.collection(VACCINES_COLLECTION).document(vaccineId).set(vaccine)
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }

    override suspend fun deleteVaccine(id: String): ResponseResult<Unit> {
        return try {
            db.collection(VACCINES_COLLECTION).document(id).delete()
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }
}
