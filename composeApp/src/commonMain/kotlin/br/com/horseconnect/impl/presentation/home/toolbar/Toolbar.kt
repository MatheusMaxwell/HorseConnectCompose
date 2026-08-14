package br.com.horseconnect.impl.presentation.home.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.dimens.fontsize.FontSize
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.shape.Shape
import br.com.horseconnect.theme.dimens.size.CustomSize
import horseconnect.composeapp.generated.resources.Res
import horseconnect.composeapp.generated.resources.baseline_more_vert_24
import horseconnect.composeapp.generated.resources.icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Toolbar(
    farmName: String,
    unreadNotificationsCount: Int = 0,
    onClickNotifications: () -> Unit = {},
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(HorseColor.background)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Padding.large, vertical = Padding.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier
                        .size(CustomSize.iconButtonSize)
                        .clip(Shape.circle)
                        .background(HorseColor.surface)
                        .border(CustomSize.borderThin, HorseColor.border, Shape.circle)
                        .padding(Padding.small),
                    painter = painterResource(Res.drawable.icon),
                    contentDescription = "",
                    tint = HorseColor.accent
                )
                Spacer(modifier = Modifier.width(Padding.medium))
                Column {
                    Text(
                        text = "Haras",
                        style = TextStyle(color = HorseColor.textSecondary, fontSize = FontSize.ultraSmall)
                    )
                    Text(
                        text = farmName,
                        style = TextStyle(
                            color = HorseColor.textPrimary,
                            fontSize = FontSize.small,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                NotificationBell(
                    unreadCount = unreadNotificationsCount,
                    onClick = onClickNotifications
                )
                Spacer(modifier = Modifier.width(Padding.small))
                MoreMenu(
                    onLogoutClick = onLogout
                )
            }
        }
        Divider(color = HorseColor.border, thickness = CustomSize.borderThin)
    }
}

@Composable
private fun NotificationBell(
    unreadCount: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(CustomSize.toolbarActionIconSize)
            .clickable { onClick.invoke() }
    ) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notificações",
            tint = HorseColor.textSecondary
        )
        if (unreadCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(CustomSize.badgeSize)
                    .clip(Shape.circle)
                    .background(HorseColor.error),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (unreadCount > 9) "9+" else unreadCount.toString(),
                    style = TextStyle(color = HorseColor.white, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun MoreMenu(
    onLogoutClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        Icon(
            modifier = Modifier
                .size(CustomSize.toolbarActionIconSize)
                .clickable { expanded = true },
            painter = painterResource(Res.drawable.baseline_more_vert_24),
            contentDescription = "",
            tint = HorseColor.textSecondary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onLogoutClick()
                },
                content = {
                    Text("Sair", color = HorseColor.textPrimary)
                }
            )
        }
    }
}

@Preview
@Composable
private fun ToolbarPreview() {
    HorseTheme {
        Toolbar(
            farmName = "Haras Teste",
            onLogout = {}
        )
    }
}