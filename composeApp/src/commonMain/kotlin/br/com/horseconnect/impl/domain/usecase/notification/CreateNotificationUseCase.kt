package br.com.horseconnect.impl.domain.usecase.notification

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.notification.NotificationRepository
import br.com.horseconnect.impl.domain.model.Notification

class CreateNotificationUseCase(
    private val repository: NotificationRepository
): BaseUseCase<Notification, Unit> {
    override suspend fun invoke(params: Notification?): ResponseResult<Unit> {
        return params?.let {
            repository.addNotification(it)
        } ?: ResponseResult.Error(CommonErrors.UnknownError())
    }
}
