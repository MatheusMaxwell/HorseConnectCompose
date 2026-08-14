package br.com.horseconnect.impl.data.mapper

import br.com.horseconnect.impl.data.model.NotificationData
import br.com.horseconnect.impl.domain.model.Notification
import br.com.horseconnect.impl.domain.model.NotificationType
import br.com.horseconnect.impl.domain.model.RelatedEntityType

fun NotificationData.toDomain(id: String): Notification {
    return Notification(
        id = id,
        userId = userId,
        type = NotificationType.enumOf(type),
        title = title,
        message = message,
        createdAt = createdAt,
        read = read,
        relatedEntityId = relatedEntityId.orEmpty(),
        relatedEntityType = RelatedEntityType.enumOfOrNull(relatedEntityType),
        dueDate = dueDate.orEmpty()
    )
}
