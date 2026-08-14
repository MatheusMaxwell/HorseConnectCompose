package br.com.horseconnect.impl.domain.mapper

import br.com.horseconnect.impl.data.model.VaccineData
import br.com.horseconnect.impl.domain.model.Vaccine

fun Vaccine.toData(): VaccineData {
    return VaccineData(
        farmId = farmId,
        name = name,
        repeatIntervalValue = repeatIntervalValue,
        repeatIntervalUnit = repeatIntervalUnit.name,
        applicationDate = applicationDate,
        nextDueDate = nextDueDate,
        veterinarian = veterinarian.ifEmpty { null },
        batch = batch.ifEmpty { null },
        notes = notes.ifEmpty { null },
        animalId = animalId,
        animalName = animalName
    )
}
