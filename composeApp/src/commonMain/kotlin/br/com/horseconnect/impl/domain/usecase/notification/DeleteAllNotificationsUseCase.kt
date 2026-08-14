package br.com.horseconnect.impl.domain.usecase.notification

import br.com.horseconnect.common.base.usecase.BaseUseCase
import br.com.horseconnect.common.error.CommonErrors
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.data.repository.notification.NotificationRepository

class DeleteAllNotificationsUseCase(
    private val repository: NotificationRepository
): BaseUseCase<List<String>, Unit> {
    override suspend fun invoke(params: List<String>?): ResponseResult<Unit> {
        return params?.let {
            repository.deleteNotifications(it)
        } ?: ResponseResult.Error(CommonErrors.UnknownError())
    }
}
