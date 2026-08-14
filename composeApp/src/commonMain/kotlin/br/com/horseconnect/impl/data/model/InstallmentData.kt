package br.com.horseconnect.impl.data.model

import kotlinx.serialization.Serializable

@Serializable
data class InstallmentData(
    val number: Int,
    val amount: Double,
    val dueDate: String
)
