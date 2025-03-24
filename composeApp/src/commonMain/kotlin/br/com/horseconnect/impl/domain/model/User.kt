package br.com.horseconnect.impl.domain.model

import kotlinx.serialization.Serializable

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String
)