package br.com.horseconnect.impl.domain.usecase.notification

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.notification.NotificationRepository

class DeleteNotificationUseCase(
    private val repository: NotificationRepository
): BaseUseCase<String, Unit> {
    override suspend fun invoke(params: String?): ResponseResult<Unit> {
        return params?.let {
            repository.deleteNotification(it)
        } ?: ResponseResult.Error(CommonErrors.UnknownError())
    }
}
