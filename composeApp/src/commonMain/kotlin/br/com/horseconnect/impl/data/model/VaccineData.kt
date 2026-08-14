package br.com.horseconnect.impl.data.model

import kotlinx.serialization.Serializable

@Serializable
data class VaccineData(
    val farmId: String,
    val name: String,
    val repeatIntervalValue: Int,
    val repeatIntervalUnit: String,
    val applicationDate: String,
    val nextDueDate: String,
    val veterinarian: String? = null,
    val batch: String? = null,
    val notes: String? = null,
    val animalId: String,
    val animalName: String
)
