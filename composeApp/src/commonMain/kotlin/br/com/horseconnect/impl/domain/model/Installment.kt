package br.com.horseconnect.impl.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Installment(
    val number: Int,
    val amount: Double,
    val dueDate: String
) {
    companion object {
        val mock = Installment(
            number = 1,
            amount = 250.0,
            dueDate = "2026-09-01T12:00:00Z"
        )
    }
}
