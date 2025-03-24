package br.com.horseconnect.impl.data.datasource.animal

import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.ext.toGenericError
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.mapper.toDomain
import br.com.horseconnect.impl.data.model.AnimalData
import br.com.horseconnect.impl.data.model.FarmData
import br.com.horseconnect.impl.data.model.GenealogyData
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.Genealogy
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.LocalCacheSettings
import dev.gitlive.firebase.firestore.Source
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.firestoreSettings

private const val ANIMALS_COLLECTION = "animals"
private const val  FARM_ID_FIELD = "farmId"
private const val GENEALOGY_COLLECTION = "genealogies"

class AnimalDataSourceImpl : AnimalDataSource {
    private val db = Firebase.firestore

    init {
        db.settings = firestoreSettings {
            cacheSettings = LocalCacheSettings.Persistent.newBuilder().build()
        }
    }

    override suspend fun getAnimalsByFarmId(farmId: String): ResponseResult<List<Animal>> {
        return try {
            val snapshot = db
                .collection(ANIMALS_COLLECTION)
                .where { FARM_ID_FIELD equalTo farmId }
                .get()

            val animals = snapshot.documents.map {
                val data = it.data(strategy = AnimalData.serializer())
                data.toDomain(it.id)
            }
            ResponseResult.Success(animals)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }

    override suspend fun getAnimalById(animalId: String): ResponseResult<Animal> {
        return try {
            val snapshot = db
                .collection(ANIMALS_COLLECTION)
                .document(animalId)
                .get(Source.CACHE)

            if(snapshot.exists) {
                val animal = snapshot.data(strategy = AnimalData.serializer())
                ResponseResult.Success(animal.toDomain(animalId))
            } else {
                ResponseResult.Error(CommonErrors.NotFound())
            }
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }

    override suspend fun getAnimalGenealogyByAnimalId(
        animalId: String
    ): ResponseResult<Genealogy> {
        return try {
            val snapshot = db
                .collection(GENEALOGY_COLLECTION)
                .document(animalId)
                .get()

            val genealogy = snapshot.data(strategy = GenealogyData.serializer()).copy(id = animalId)

            ResponseResult.Success(genealogy.toDomain())
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }

    override suspend fun registerAnimal(animal: AnimalData): ResponseResult<Unit> {
        return try {
            db.collection(ANIMALS_COLLECTION).add(animal)
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e.toGenericError())
        }
    }
}