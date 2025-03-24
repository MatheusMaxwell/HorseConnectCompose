package br.com.horseconnect.impl.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AnimalData(
    val birthDate: String,
    val coat: String,
    val farmId: String,
    val imageId: String,
    val imageUrl: String,
    val isLive: Boolean,
    val name: String,
    val race: String,
    val sex: String,
    val types: String,
    val ownerOf: Double,
    val otherOwners: List<OwnerData>?
)

@Serializable
data class OwnerData(
    val name: String,
    val ownerOf: Double
)
