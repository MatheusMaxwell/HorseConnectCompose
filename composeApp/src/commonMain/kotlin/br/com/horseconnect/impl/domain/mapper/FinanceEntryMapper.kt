package br.com.horseconnect.impl.domain.mapper

import br.com.horseconnect.impl.data.model.FinanceEntryData
import br.com.horseconnect.impl.data.model.InstallmentData
import br.com.horseconnect.impl.domain.model.FinanceEntry
import br.com.horseconnect.impl.domain.model.Installment

fun FinanceEntry.toData(): FinanceEntryData {
    return FinanceEntryData(
        farmId = farmId,
        type = type.name,
        description = description,
        category = category,
        amount = amount,
        date = date,
        isRecurring = isRecurring,
        recurrenceIntervalValue = recurrenceIntervalValue,
        recurrenceIntervalUnit = recurrenceIntervalUnit.name,
        recurrenceEndDate = recurrenceEndDate.ifEmpty { null },
        isInstallment = isInstallment,
        installments = installments.map { it.toData() },
        notes = notes.ifEmpty { null }
    )
}

private fun Installment.toData(): InstallmentData {
    return InstallmentData(
        number = number,
        amount = amount,
        dueDate = dueDate
    )
}
