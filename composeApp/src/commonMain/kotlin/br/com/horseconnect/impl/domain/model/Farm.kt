package br.com.horseconnect.impl.domain.model

import kotlinx.serialization.Serializable

data class Farm(
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