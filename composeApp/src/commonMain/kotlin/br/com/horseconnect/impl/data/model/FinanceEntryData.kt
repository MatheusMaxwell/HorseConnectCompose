package br.com.horseconnect.impl.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FinanceEntryData(
    val farmId: String,
    val type: String,
    val description: String,
    val category: String,
    val amount: Double,
    val date: String,
    val isRecurring: Boolean,
    val recurrenceIntervalValue: Int,
    val recurrenceIntervalUnit: String,
    val recurrenceEndDate: String? = null,
    val isInstallment: Boolean,
    val installments: List<InstallmentData> = emptyList(),
    val notes: String? = null
)
