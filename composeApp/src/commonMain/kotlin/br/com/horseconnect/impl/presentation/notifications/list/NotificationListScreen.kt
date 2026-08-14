package br.com.horseconnect.impl.presentation.notifications.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.impl.domain.model.Notification
import br.com.horseconnect.impl.presentation.notifications.list.content.NotificationListContent
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationListScreen(
    onItemClick: (Notification) -> Unit,
    popBackStack: () -> Unit
) {
    val viewModel = koinViewModel<NotificationListViewModel>()
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val loadingDialog = LocalLoadingDialog.current

    LaunchedEffect(Unit) {
        viewModel.getNotifications()
    }

    NotificationListContent(
        viewState = viewState,
        loadingDialogManager = loadingDialog,
        onItemClick = { notification ->
            viewModel.deleteNotification(notification)
            onItemClick.invoke(notification)
        },
        onClearAll = viewModel::deleteAllNotifications,
        popBackStack = popBackStack
    )
}
