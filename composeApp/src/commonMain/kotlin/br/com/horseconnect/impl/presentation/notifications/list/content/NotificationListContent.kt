package br.com.horseconnect.impl.presentation.notifications.list.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.common.viewstate.BaseViewState
import br.com.horseconnect.impl.domain.model.Notification
import br.com.horseconnect.impl.domain.model.NotificationType
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.loading.LoadingDialogManager
import br.com.horseconnect.theme.components.toolbar.Toolbar
import br.com.horseconnect.theme.dimens.fontsize.FontSize
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.shape.Shape
import br.com.horseconnect.theme.dimens.size.CustomSize
import horseconnect.composeapp.generated.resources.Res
import horseconnect.composeapp.generated.resources.baseline_health_24
import horseconnect.composeapp.generated.resources.baseline_wallet_24
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NotificationListContent(
    viewState: BaseViewState<List<Notification>>,
    loadingDialogManager: LoadingDialogManager,
    onItemClick: (Notification) -> Unit,
    onClearAll: (List<Notification>) -> Unit,
    popBackStack: () -> Unit
) {
    var notifications: List<Notification> by remember { mutableStateOf(emptyList()) }
    val toaster = LocalToasterDialog.current

    LaunchedEffect(viewState) {
        when(viewState) {
            BaseViewState.Initial -> Unit
            BaseViewState.Loading -> {
                loadingDialogManager.show()
            }
            is BaseViewState.Populated -> {
                loadingDialogManager.hide()
                notifications = viewState.data
            }
            is BaseViewState.Error -> {
                loadingDialogManager.hide()
                toaster.show(viewState.msg)
            }
        }
    }

    Scaffold(
        topBar = {
            Toolbar(
                title = "Notificações",
                onNavigationAction = {
                    popBackStack.invoke()
                },
                actions = {
                    if (notifications.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .clickable { onClearAll.invoke(notifications) }
                                .padding(horizontal = Padding.large),
                            text = "Limpar tudo",
                            style = TextStyle(color = HorseColor.accent, fontSize = FontSize.ultraSmall, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            )
        },
        backgroundColor = HorseColor.background
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(Padding.large)) {
            if (notifications.isEmpty()) {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(top = Padding.x2Large),
                    text = "Nenhuma notificação por aqui.",
                    textAlign = TextAlign.Center,
                    style = TextStyle(color = HorseColor.textMuted, fontSize = FontSize.ultraSmall)
                )
            }

            LazyColumn {
                items(notifications) { notification ->
                    NotificationRow(notification) {
                        onItemClick.invoke(it)
                    }
                    Spacer(modifier = Modifier.height(Padding.small))
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(notification: Notification, onClick: (Notification) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke(notification) },
        elevation = 0.dp,
        shape = Shape.medium,
        backgroundColor = HorseColor.surface,
        border = BorderStroke(width = CustomSize.borderThin, color = HorseColor.accent)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(Padding.large),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(CustomSize.animalCardImageSize / 2)
                    .clip(Shape.small)
                    .background(HorseColor.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                NotificationTypeIcon(notification.type)
            }

            Spacer(modifier = Modifier.width(Padding.medium))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    style = TextStyle(
                        color = HorseColor.textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = FontSize.ultraSmall
                    )
                )
                Text(
                    text = notification.message,
                    style = TextStyle(color = HorseColor.textSecondary, fontSize = FontSize.ultraSmall)
                )
                Text(
                    text = DateUtil.getDateFormatter(notification.createdAt),
                    style = TextStyle(color = HorseColor.textMuted, fontSize = FontSize.ultraSmall)
                )
            }
        }
    }
}

@Composable
private fun NotificationTypeIcon(type: NotificationType) {
    when(type) {
        NotificationType.VACCINE_PENDING, NotificationType.VACCINE_DUE -> {
            Icon(
                painter = painterResource(Res.drawable.baseline_health_24),
                contentDescription = "",
                tint = HorseColor.accent
            )
        }
        NotificationType.FINANCE_RECURRENCE_ENDING,
        NotificationType.FINANCE_NEGATIVE_BALANCE,
        NotificationType.FINANCE_EXPENSE_DUE,
        NotificationType.FINANCE_INCOME_DUE -> {
            Icon(
                painter = painterResource(Res.drawable.baseline_wallet_24),
                contentDescription = "",
                tint = HorseColor.accent
            )
        }
        NotificationType.PREGNANCY_DUE_SOON, NotificationType.PREGNANCY_STATUS_STALE -> {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "",
                tint = HorseColor.accent
            )
        }
        NotificationType.GENERIC -> {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "",
                tint = HorseColor.accent
            )
        }
    }
}

@Preview
@Composable
private fun NotificationListContentPreview() {
    HorseTheme {
        NotificationListContent(
            viewState = BaseViewState.Populated(listOf(Notification.mock, Notification.mock.copy(id = "2"))),
            loadingDialogManager = LoadingDialogManager(),
            onItemClick = {},
            onClearAll = {},
            popBackStack = {}
        )
    }
}
