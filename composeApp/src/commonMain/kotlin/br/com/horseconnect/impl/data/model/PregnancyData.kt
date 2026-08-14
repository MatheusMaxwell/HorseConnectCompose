package br.com.horseconnect.impl.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PregnancyData(
    val date: String,
    val farmId: String,
    val femaleId: String,
    val femaleName: String,
    val maleId: String,
    val maleName: String,
    val receiverId: String,
    val sex: String,
    val status: String,
    val type: String
)
