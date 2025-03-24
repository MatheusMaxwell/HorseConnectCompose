package br.com.horseconnect.impl.data.datasource.animal

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.model.AnimalData
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.Genealogy

interface AnimalDataSource {

    suspend fun getAnimalsByFarmId(farmId: String): ResponseResult<List<Animal>>
    suspend fun getAnimalById(animalId: String): ResponseResult<Animal>
    suspend fun getAnimalGenealogyByAnimalId(animalId: String): ResponseResult<Genealogy>
    suspend fun registerAnimal(animal: AnimalData): ResponseResult<Unit>
}