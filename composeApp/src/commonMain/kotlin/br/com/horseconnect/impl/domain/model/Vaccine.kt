package br.com.horseconnect.impl.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Vaccine(
    val id: String,
    val farmId: String,
    val name: String,
    val repeatIntervalValue: Int,
    val repeatIntervalUnit: VaccineIntervalUnit,
    val applicationDate: String,
    val nextDueDate: String,
    val veterinarian: String,
    val batch: String,
    val notes: String,
    val animalId: String,
    val animalName: String
) {
    companion object {
        val mock = Vaccine(
            id = "1",
            farmId = "1",
            name = "Vacina Múltipla",
            repeatIntervalValue = 6,
            repeatIntervalUnit = VaccineIntervalUnit.MONTHS,
            applicationDate = "2026-02-01T12:00:00Z",
            nextDueDate = "2026-08-01T12:00:00Z",
            veterinarian = "Dr. João",
            batch = "L123",
            notes = "",
            animalId = "1",
            animalName = "Romeiro de Alcatéia"
        )
    }
}

enum class VaccineIntervalUnit(val label: String) {
    DAYS("dias"),
    MONTHS("meses");

    companion object {
        fun enumOf(value: String): VaccineIntervalUnit {
            return entries.firstOrNull { it.name == value } ?: MONTHS
        }
    }
}
