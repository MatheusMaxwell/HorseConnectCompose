package br.com.horseconnect.impl.data.repository.notification

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Notification

interface NotificationRepository {

    suspend fun getNotifications(): ResponseResult<List<Notification>>
    suspend fun addNotification(notification: Notification): ResponseResult<Unit>
    suspend fun deleteNotification(notificationId: String): ResponseResult<Unit>
    suspend fun deleteNotifications(notificationIds: List<String>): ResponseResult<Unit>
}
