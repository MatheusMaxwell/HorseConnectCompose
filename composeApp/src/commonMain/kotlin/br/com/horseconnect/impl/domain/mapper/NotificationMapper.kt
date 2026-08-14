package br.com.horseconnect.impl.domain.mapper

import br.com.horseconnect.impl.data.model.NotificationData
import br.com.horseconnect.impl.domain.model.Notification

fun Notification.toData(): NotificationData {
    return NotificationData(
        userId = userId,
        type = type.name,
        title = title,
        message = message,
        createdAt = createdAt,
        read = read,
        relatedEntityId = relatedEntityId.ifEmpty { null },
        relatedEntityType = relatedEntityType?.name,
        dueDate = dueDate.ifEmpty { null }
    )
}
