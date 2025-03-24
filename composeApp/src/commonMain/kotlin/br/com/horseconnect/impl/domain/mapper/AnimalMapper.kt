package br.com.horseconnect.impl.domain.mapper

import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.impl.data.model.AnimalData
import br.com.horseconnect.impl.data.model.OwnerData
import br.com.horseconnect.impl.domain.model.Animal

fun Animal.toData(): AnimalData {
    return AnimalData(
        birthDate = DateUtil.convertToISO8601(birthDate),
        coat = coat,
        farmId = farmId,
        imageId = imageId,
        imageUrl = imageUrl,
        isLive = isLive,
        name = name,
        race = race,
        sex = sex,
        types = types.joinToString(",") { it.value },
        ownerOf = ownerOf,
        otherOwners = otherOwners.map { OwnerData(it.name, it.ownerOf) }
    )
}