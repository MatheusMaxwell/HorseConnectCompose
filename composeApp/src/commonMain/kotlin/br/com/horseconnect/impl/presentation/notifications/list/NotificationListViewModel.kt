package br.com.horseconnect.impl.presentation.notifications.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.common.viewstate.BaseViewState
import br.com.horseconnect.impl.domain.model.Notification
import br.com.horseconnect.impl.domain.usecase.notification.DeleteAllNotificationsUseCase
import br.com.horseconnect.impl.domain.usecase.notification.DeleteNotificationUseCase
import br.com.horseconnect.impl.domain.usecase.notification.GetNotificationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationListViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
    private val deleteAllNotificationsUseCase: DeleteAllNotificationsUseCase
): ViewModel() {

    private val _state: MutableStateFlow<BaseViewState<List<Notification>>> = MutableStateFlow(
        BaseViewState.Initial
    )
    val state: StateFlow<BaseViewState<List<Notification>>> get() = _state

    fun getNotifications() {
        _state.tryEmit(BaseViewState.Loading)
        viewModelScope.launch {
            when(val result = getNotificationsUseCase.invoke()) {
                is ResponseResult.Success -> {
                    _state.tryEmit(BaseViewState.Populated(result.data.sortedByDescending { it.createdAt }))
                }
                is ResponseResult.Error -> {
                    _state.tryEmit(BaseViewState.Error(result.resultError.msg))
                }
            }
        }
    }

    fun deleteNotification(notification: Notification) {
        viewModelScope.launch {
            deleteNotificationUseCase.invoke(notification.id)
            getNotifications()
        }
    }

    fun deleteAllNotifications(notifications: List<Notification>) {
        val ids = notifications.map { it.id }
        if (ids.isEmpty()) return
        viewModelScope.launch {
            deleteAllNotificationsUseCase.invoke(ids)
            getNotifications()
        }
    }
}
