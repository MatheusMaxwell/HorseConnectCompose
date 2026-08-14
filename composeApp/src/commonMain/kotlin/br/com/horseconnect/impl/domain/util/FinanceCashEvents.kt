package br.com.horseconnect.impl.domain.util

import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.impl.domain.model.FinanceEntry
import br.com.horseconnect.impl.domain.model.FinanceIntervalUnit
import br.com.horseconnect.impl.domain.model.FinanceType

data class CashEvent(val date: String, val amount: Double, val type: FinanceType)

// Splits installments/recurring entries into per-occurrence events so consumers only count amounts on their actual dates, not the full committed total.
fun FinanceEntry.cashEvents(): List<CashEvent> {
    return when {
        isInstallment -> installments.map { CashEvent(it.dueDate, it.amount, type) }
        isRecurring -> {
            val boundary = DateUtil.addMonths(DateUtil.now(), 1)
            val step = recurrenceIntervalValue.coerceAtLeast(1)
            val events = mutableListOf<CashEvent>()
            var occurrenceDate = date
            var safetyCount = 0
            while (occurrenceDate <= boundary && safetyCount < 240) {
                if (recurrenceEndDate.isNotEmpty() && occurrenceDate > recurrenceEndDate) break
                events.add(CashEvent(occurrenceDate, amount, type))
                occurrenceDate = if (recurrenceIntervalUnit == FinanceIntervalUnit.MONTHS) {
                    DateUtil.addMonths(occurrenceDate, step)
                } else {
                    DateUtil.addDays(occurrenceDate, step)
                }
                safetyCount++
            }
            events
        }
        else -> listOf(CashEvent(date, amount, type))
    }
}

fun List<FinanceEntry>.toCashEvents(): List<CashEvent> = flatMap { it.cashEvents() }

fun List<CashEvent>.balance(): Double =
    sumOf { if (it.type == FinanceType.INCOME) it.amount else -it.amount }
