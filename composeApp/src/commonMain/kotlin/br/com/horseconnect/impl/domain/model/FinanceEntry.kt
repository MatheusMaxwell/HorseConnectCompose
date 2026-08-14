package br.com.horseconnect.impl.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FinanceEntry(
    val id: String,
    val farmId: String,
    val type: FinanceType,
    val description: String,
    val category: String,
    val amount: Double,
    val date: String,
    val isRecurring: Boolean,
    val recurrenceIntervalValue: Int,
    val recurrenceIntervalUnit: FinanceIntervalUnit,
    val recurrenceEndDate: String,
    val isInstallment: Boolean,
    val installments: List<Installment>,
    val notes: String
) {
    companion object {
        val mock = FinanceEntry(
            id = "1",
            farmId = "1",
            type = FinanceType.EXPENSE,
            description = "Ração mensal",
            category = "Ração",
            amount = 850.0,
            date = "2026-08-01T12:00:00Z",
            isRecurring = true,
            recurrenceIntervalValue = 1,
            recurrenceIntervalUnit = FinanceIntervalUnit.MONTHS,
            recurrenceEndDate = "",
            isInstallment = false,
            installments = emptyList(),
            notes = ""
        )
    }
}

enum class FinanceType(val label: String) {
    INCOME("Entrada"),
    EXPENSE("Saída");

    companion object {
        fun enumOf(value: String): FinanceType {
            return entries.firstOrNull { it.name == value } ?: EXPENSE
        }
    }
}

enum class FinanceIntervalUnit(val label: String) {
    DAYS("dias"),
    MONTHS("meses");

    companion object {
        fun enumOf(value: String): FinanceIntervalUnit {
            return entries.firstOrNull { it.name == value } ?: MONTHS
        }
    }
}
