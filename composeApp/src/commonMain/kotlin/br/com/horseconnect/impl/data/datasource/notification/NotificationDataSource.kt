package br.com.horseconnect.impl.data.datasource.notification

import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.model.NotificationData
import br.com.horseconnect.impl.domain.model.Notification

interface NotificationDataSource {

    suspend fun getNotifications(userId: String): ResponseResult<List<Notification>>
    suspend fun addNotification(notification: NotificationData): ResponseResult<Unit>
    suspend fun deleteNotification(notificationId: String): ResponseResult<Unit>
    suspend fun deleteNotifications(notificationIds: List<String>): ResponseResult<Unit>
}
