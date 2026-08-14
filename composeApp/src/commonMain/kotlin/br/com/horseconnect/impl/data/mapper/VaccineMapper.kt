package br.com.horseconnect.impl.data.mapper

import br.com.horseconnect.impl.data.model.VaccineData
import br.com.horseconnect.impl.domain.model.Vaccine
import br.com.horseconnect.impl.domain.model.VaccineIntervalUnit

fun VaccineData.toDomain(id: String): Vaccine {
    return Vaccine(
        id = id,
        farmId = farmId,
        name = name,
        repeatIntervalValue = repeatIntervalValue,
        repeatIntervalUnit = VaccineIntervalUnit.enumOf(repeatIntervalUnit),
        applicationDate = applicationDate,
        nextDueDate = nextDueDate,
        veterinarian = veterinarian.orEmpty(),
        batch = batch.orEmpty(),
        notes = notes.orEmpty(),
        animalId = animalId,
        animalName = animalName
    )
}
