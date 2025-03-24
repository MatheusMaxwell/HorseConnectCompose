package br.com.horseconnect.impl.data.repository.animal

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.Genealogy

interface AnimalRepository {

    suspend fun getAnimals(): ResponseResult<List<Animal>>
    suspend fun getAnimalById(animalId: String): ResponseResult<Animal>
    suspend fun getAnimalGenealogyByAnimalId(animalId: String): ResponseResult<Genealogy>
    suspend fun registerAnimal(animal: Animal): ResponseResult<Unit>
}