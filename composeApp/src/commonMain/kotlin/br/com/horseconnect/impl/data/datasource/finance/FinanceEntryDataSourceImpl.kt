package br.com.horseconnect.impl.data.datasource.finance

import br.com.horseconnect.common.ext.toGenericError
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.mapper.toDomain
import br.com.horseconnect.impl.data.model.FinanceEntryData
import br.com.horseconnect.impl.domain.model.FinanceEntry
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.LocalCacheSettings
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.firestoreSettings

private const val FINANCE_ENTRIES_COLLECTION = "finance_entries"
private const val FARM_ID_FIELD = "farmId"

class FinanceEntryDataSourceImpl: FinanceEntryDataSource {

    private val db = Firebase.firestore

    init {
        db.settings = firestoreSettings {
            cacheSettings = LocalCacheSettings.Persistent.newBuilder().build()
        }
    }

    override suspend fun getFinanceEntries(farmId: String): ResponseResult<List<FinanceEntry>> {
        return try {
            val snapshot = db
                .collection(FINANCE_ENTRIES_COLLECTION)
                .where { FARM_ID_FIELD equalTo farmId }
                .get()

            val entries = snapshot.documents.map {
                val data = it.data(strategy = FinanceEntryData.serializer())
                data.toDomain(it.id)
            }
            ResponseResult.Success(entries)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }

    override suspend fun addFinanceEntry(entry: FinanceEntryData): ResponseResult<Unit> {
        return try {
            db.collection(FINANCE_ENTRIES_COLLECTION).add(entry)
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }

    override suspend fun updateFinanceEntry(entry: FinanceEntryData, entryId: String): ResponseResult<Unit> {
        return try {
            db.collection(FINANCE_ENTRIES_COLLECTION).document(entryId).set(entry)
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }

    override suspend fun deleteFinanceEntry(id: String): ResponseResult<Unit> {
        return try {
            db.collection(FINANCE_ENTRIES_COLLECTION).document(id).delete()
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }
}
