package br.com.horseconnect.impl.data.repository.notification

import br.com.horseconnect.common.manager.SessionManager
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.datasource.notification.NotificationDataSource
import br.com.horseconnect.impl.domain.mapper.toData
import br.com.horseconnect.impl.domain.model.Notification

class NotificationRepositoryImpl(
    private val dataSource: NotificationDataSource
): NotificationRepository {
    override suspend fun getNotifications(): ResponseResult<List<Notification>> {
        return dataSource.getNotifications(SessionManager.user?.id.orEmpty())
    }

    override suspend fun addNotification(notification: Notification): ResponseResult<Unit> {
        return dataSource.addNotification(notification.toData())
    }

    override suspend fun deleteNotification(notificationId: String): ResponseResult<Unit> {
        return dataSource.deleteNotification(notificationId)
    }

    override suspend fun deleteNotifications(notificationIds: List<String>): ResponseResult<Unit> {
        return dataSource.deleteNotifications(notificationIds)
    }
}
