package br.com.horseconnect.impl.domain.util

import br.com.horseconnect.common.ext.toCurrencyString
import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.impl.domain.model.FinanceEntry
import br.com.horseconnect.impl.domain.model.FinanceType
import br.com.horseconnect.impl.domain.model.Notification
import br.com.horseconnect.impl.domain.model.NotificationType
import br.com.horseconnect.impl.domain.model.Pregnancy
import br.com.horseconnect.impl.domain.model.RelatedEntityType
import br.com.horseconnect.impl.domain.model.Vaccine

private const val VACCINE_DUE_WINDOW_DAYS = 7
private const val VACCINE_OVERDUE_WINDOW_DAYS = 30
private const val PREGNANCY_DUE_WINDOW_DAYS = 30
private const val PREGNANCY_OVERDUE_WINDOW_DAYS = 14
private const val PREGNANCY_GESTATION_DAYS = 340
private const val FINANCE_DUE_WINDOW_DAYS = 7
private val INACTIVE_PREGNANCY_STATUSES = listOf("Finalizado", "Perdido")

// Compares candidate due dates against what's already stored so the same reminder isn't recreated every time the Home screen loads.
fun generateDueNotifications(
    userId: String,
    existingNotifications: List<Notification>,
    pregnancies: List<Pregnancy>,
    vaccines: List<Vaccine>,
    financeEntries: List<FinanceEntry>
): List<Notification> {
    val existingKeys = existingNotifications.map { notificationKey(it.type, it.relatedEntityId, it.dueDate) }.toSet()
    val now = DateUtil.now()
    val candidates = mutableListOf<Notification>()

    val vaccineBoundary = DateUtil.addDays(now, VACCINE_DUE_WINDOW_DAYS)
    val vaccineOverdueBoundary = DateUtil.addDays(now, -VACCINE_OVERDUE_WINDOW_DAYS)
    vaccines.forEach { vaccine ->
        if (vaccine.nextDueDate.isEmpty() || vaccine.nextDueDate < vaccineOverdueBoundary) return@forEach
        val type = when {
            DateUtil.isPastOrToday(vaccine.nextDueDate) -> NotificationType.VACCINE_PENDING
            vaccine.nextDueDate <= vaccineBoundary -> NotificationType.VACCINE_DUE
            else -> null
        } ?: return@forEach

        candidates += buildNotification(
            userId = userId,
            type = type,
            message = "${vaccine.name} de ${vaccine.animalName} — ${DateUtil.getDateFormatter(vaccine.nextDueDate)}",
            relatedEntityId = vaccine.id,
            relatedEntityType = RelatedEntityType.VACCINE,
            dueDate = vaccine.nextDueDate
        )
    }

    val pregnancyBoundary = DateUtil.addDays(now, PREGNANCY_DUE_WINDOW_DAYS)
    val pregnancyOverdueBoundary = DateUtil.addDays(now, -PREGNANCY_OVERDUE_WINDOW_DAYS)
    pregnancies.forEach { pregnancy ->
        if (pregnancy.date.isEmpty() || pregnancy.status in INACTIVE_PREGNANCY_STATUSES) return@forEach
        // addDays keeps the original time-of-day, unlike calculateBirthForecast + convertToISO8601 which stamps the current time and made this date drift on every call.
        val forecastIso = DateUtil.addDays(pregnancy.date, PREGNANCY_GESTATION_DAYS)
        if (forecastIso > pregnancyBoundary || forecastIso < pregnancyOverdueBoundary) return@forEach

        candidates += buildNotification(
            userId = userId,
            type = NotificationType.PREGNANCY_DUE_SOON,
            message = "Parto de ${pregnancy.femaleName} previsto para ${DateUtil.getDateFormatter(forecastIso)}",
            relatedEntityId = pregnancy.id,
            relatedEntityType = RelatedEntityType.PREGNANCY,
            dueDate = forecastIso
        )
    }

    val financeBoundary = DateUtil.addDays(now, FINANCE_DUE_WINDOW_DAYS)
    financeEntries.forEach { entry ->
        val nextEvent = entry.cashEvents().filter { it.date >= now }.minByOrNull { it.date } ?: return@forEach
        if (nextEvent.date > financeBoundary) return@forEach
        val type = if (entry.type == FinanceType.EXPENSE) NotificationType.FINANCE_EXPENSE_DUE else NotificationType.FINANCE_INCOME_DUE

        candidates += buildNotification(
            userId = userId,
            type = type,
            message = "${entry.description} — ${nextEvent.amount.toCurrencyString()} em ${DateUtil.getDateFormatter(nextEvent.date)}",
            relatedEntityId = entry.id,
            relatedEntityType = RelatedEntityType.FINANCE_ENTRY,
            dueDate = nextEvent.date
        )
    }

    return candidates.filter { notificationKey(it.type, it.relatedEntityId, it.dueDate) !in existingKeys }
}

private fun notificationKey(type: NotificationType, relatedEntityId: String, dueDate: String) =
    "${type.name}|$relatedEntityId|$dueDate"

private fun buildNotification(
    userId: String,
    type: NotificationType,
    message: String,
    relatedEntityId: String,
    relatedEntityType: RelatedEntityType,
    dueDate: String
) = Notification(
    id = "",
    userId = userId,
    type = type,
    title = type.label,
    message = message,
    createdAt = DateUtil.now(),
    read = false,
    relatedEntityId = relatedEntityId,
    relatedEntityType = relatedEntityType,
    dueDate = dueDate
)
