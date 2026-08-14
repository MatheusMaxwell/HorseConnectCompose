package br.com.horseconnect.theme.components.toolbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.size.CustomSize
import horseconnect.composeapp.generated.resources.Res
import horseconnect.composeapp.generated.resources.baseline_arrow_back_24
import horseconnect.composeapp.generated.resources.baseline_more_vert_24
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Toolbar(
    title: String? = null,
    onNavigationAction: (() -> Unit)? = null,
    onLogoutAction: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {}
) {
    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        title = {
            title?.let {
                Text(it, style = TextStyle(color = HorseColor.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold))
            }
        },
        backgroundColor = HorseColor.background,
        elevation = 0.dp,
        navigationIcon = onNavigationAction?.let {
            {
                Icon(
                    modifier = Modifier
                        .padding(start = Padding.large)
                        .clickable { it.invoke() },
                    painter = painterResource(Res.drawable.baseline_arrow_back_24),
                    contentDescription = "",
                    tint = HorseColor.textPrimary
                )
            }
        },
        actions = {
            actions()
            onLogoutAction?.let {
                Box(
                    modifier = Modifier.size(CustomSize.toolbarActionIconSize).clickable {
                        it.invoke()
                    },
                ) {
                    Icon(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(Res.drawable.baseline_more_vert_24),
                        contentDescription = "",
                        tint = HorseColor.textPrimary
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun ToolbarPreview() {
    HorseTheme {
        Toolbar(
            title = "Plantel",
            onNavigationAction = {}
        )
    }
}