package br.com.horseconnect.impl.data.mapper

import br.com.horseconnect.impl.data.model.FinanceEntryData
import br.com.horseconnect.impl.data.model.InstallmentData
import br.com.horseconnect.impl.domain.model.FinanceEntry
import br.com.horseconnect.impl.domain.model.FinanceIntervalUnit
import br.com.horseconnect.impl.domain.model.FinanceType
import br.com.horseconnect.impl.domain.model.Installment

fun FinanceEntryData.toDomain(id: String): FinanceEntry {
    return FinanceEntry(
        id = id,
        farmId = farmId,
        type = FinanceType.enumOf(type),
        description = description,
        category = category,
        amount = amount,
        date = date,
        isRecurring = isRecurring,
        recurrenceIntervalValue = recurrenceIntervalValue,
        recurrenceIntervalUnit = FinanceIntervalUnit.enumOf(recurrenceIntervalUnit),
        recurrenceEndDate = recurrenceEndDate.orEmpty(),
        isInstallment = isInstallment,
        installments = installments.map { it.toDomain() },
        notes = notes.orEmpty()
    )
}

private fun InstallmentData.toDomain(): Installment {
    return Installment(
        number = number,
        amount = amount,
        dueDate = dueDate
    )
}
