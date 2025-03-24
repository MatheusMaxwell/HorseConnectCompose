package br.com.horseconnect.impl.data.repository.animal

import br.com.horseconnect.common.manager.SessionManager
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.datasource.animal.AnimalDataSource
import br.com.horseconnect.impl.domain.mapper.toData
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.Genealogy

class AnimalRepositoryImpl(
    private val dataSource: AnimalDataSource
): AnimalRepository {
    override suspend fun getAnimals(): ResponseResult<List<Animal>> {
       return dataSource.getAnimalsByFarmId(SessionManager.farm?.id.orEmpty())
    }

    override suspend fun getAnimalById(animalId: String): ResponseResult<Animal> {
        return dataSource.getAnimalById(animalId)
    }

    override suspend fun getAnimalGenealogyByAnimalId(animalId: String): ResponseResult<Genealogy> {
        return dataSource.getAnimalGenealogyByAnimalId(animalId)
    }

    override suspend fun registerAnimal(animal: Animal): ResponseResult<Unit> {
        return dataSource.registerAnimal(animal.toData())
    }
}