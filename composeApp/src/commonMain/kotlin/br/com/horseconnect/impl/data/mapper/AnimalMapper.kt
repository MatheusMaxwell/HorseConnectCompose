package br.com.horseconnect.impl.data.mapper

import br.com.horseconnect.impl.data.model.AnimalData
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.AnimalType
import br.com.horseconnect.impl.domain.model.Owner

fun AnimalData.toDomain(id: String): Animal {
    return Animal(
        id = id,
        birthDate = birthDate,
        coat = coat,
        farmId = farmId,
        imageId = imageId,
        imageUrl = imageUrl,
        isLive = isLive,
        name = name,
        race = race,
        sex = sex,
        types = types.split(",").map { AnimalType.enumOf(it) },
        ownerOf = ownerOf,
        otherOwners = otherOwners?.map { Owner(it.name, it.ownerOf) }.orEmpty()
    )
}
