package br.com.horseconnect.impl.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FarmData(
    val id: String,
    val name: String,
    val address: String,
    val city: String,
    val state: String,
    val country: String,
    val creation: List<String>,
    val imageLogoUrl: String,
    val primaryColor: String,
    val proprietaryId: String
)

