package br.com.horseconnect.impl.domain.usecase.notification

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.notification.NotificationRepository
import br.com.horseconnect.impl.domain.model.Notification

class GetNotificationsUseCase(
    private val repository: NotificationRepository
): BaseUseCase<Unit, List<Notification>> {
    override suspend fun invoke(params: Unit?): ResponseResult<List<Notification>> {
        return repository.getNotifications()
    }
}
