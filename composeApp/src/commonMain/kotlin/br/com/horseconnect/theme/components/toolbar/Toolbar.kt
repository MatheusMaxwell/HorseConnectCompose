package br.com.horseconnect.theme.components.toolbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.size.CustomSize
import horseconnect.composeapp.generated.resources.Res
import horseconnect.composeapp.generated.resources.baseline_arrow_back_24
import horseconnect.composeapp.generated.resources.baseline_more_vert_24
import org.jetbrains.compose.resources.painterResource

@Composable
fun Toolbar(
    title: String? = null,
    onNavigationAction: (() -> Unit)? = null,
    onLogoutAction: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            title?.let {
                Text(it, style = TextStyle(color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold))
            }
        },
        backgroundColor = HorseColor.primary,
        navigationIcon = {
            onNavigationAction?.let {
                Icon(
                    modifier = Modifier
                        .padding(start = Padding.large)
                        .clickable { it.invoke() },
                    painter = painterResource(Res.drawable.baseline_arrow_back_24),
                    contentDescription = "",
                    tint = Color.White
                )
            }
        },
        actions = {
            onLogoutAction?.let {
                Box(
                    modifier = Modifier.size(CustomSize.toolbarIconSize).clickable {
                        it.invoke()
                    },
                ) {
                    Icon(
                        modifier = Modifier.align(Alignment.Center).padding(start = Padding.large),
                        painter = painterResource(Res.drawable.baseline_more_vert_24),
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }
        }
    )
}