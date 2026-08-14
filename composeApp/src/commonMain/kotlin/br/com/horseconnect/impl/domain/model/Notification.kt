package br.com.horseconnect.impl.domain.model

data class Notification(
    val id: String,
    val userId: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val createdAt: String,
    val read: Boolean,
    val relatedEntityId: String,
    val relatedEntityType: RelatedEntityType?,
    val dueDate: String
) {
    companion object {
        val mock = Notification(
            id = "1",
            userId = "1",
            type = NotificationType.VACCINE_PENDING,
            title = "Vacina pendente",
            message = "Romeiro de Alcatéia ainda não foi vacinado com Vacina Múltipla.",
            createdAt = "2026-08-05T12:00:00Z",
            read = false,
            relatedEntityId = "1",
            relatedEntityType = RelatedEntityType.ANIMAL,
            dueDate = ""
        )
    }
}

enum class NotificationType(val label: String) {
    VACCINE_PENDING("Vacina pendente"),
    VACCINE_DUE("Vacina vencendo"),
    FINANCE_RECURRENCE_ENDING("Lançamento recorrente terminando"),
    FINANCE_NEGATIVE_BALANCE("Saldo negativo"),
    FINANCE_EXPENSE_DUE("Despesa vencendo"),
    FINANCE_INCOME_DUE("Entrada a receber"),
    PREGNANCY_DUE_SOON("Parto próximo"),
    PREGNANCY_STATUS_STALE("Cruzamento sem atualização"),
    GENERIC("Geral");

    companion object {
        fun enumOf(value: String): NotificationType {
            return entries.firstOrNull { it.name == value } ?: GENERIC
        }
    }
}

enum class RelatedEntityType {
    ANIMAL,
    PREGNANCY,
    VACCINE,
    FINANCE_ENTRY;

    companion object {
        fun enumOfOrNull(value: String?): RelatedEntityType? {
            return entries.firstOrNull { it.name == value }
        }
    }
}
