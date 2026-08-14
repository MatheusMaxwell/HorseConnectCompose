package br.com.horseconnect.impl.presentation.home.quickactions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
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
fun QuickActionsContent(
    onClickPlantel: () -> Unit,
    onClickVacinas: () -> Unit,
    onClickCruzamentos: () -> Unit,
    onClickFinancas: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = Padding.medium),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        QuickActionItem(label = "Plantel", icon = Icons.AutoMirrored.Filled.List, onClick = onClickPlantel)
        QuickActionItem(label = "Vacinas", painter = painterResource(Res.drawable.baseline_health_24), onClick = onClickVacinas)
        QuickActionItem(label = "Cruzamentos", icon = Icons.Default.Favorite, onClick = onClickCruzamentos)
        QuickActionItem(label = "Finanças", painter = painterResource(Res.drawable.baseline_wallet_24), onClick = onClickFinancas)
    }
}

@Composable
private fun QuickActionItem(
    label: String,
    icon: ImageVector? = null,
    painter: Painter? = null,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable { onClick.invoke() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .size(CustomSize.quickActionSize)
                .background(HorseColor.surface, Shape.medium)
                .border(BorderStroke(CustomSize.borderThin, HorseColor.border), Shape.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = HorseColor.accent,
                    modifier = Modifier.size(CustomSize.toolbarActionIconSize)
                )
            } else if (painter != null) {
                Icon(
                    painter = painter,
                    contentDescription = label,
                    tint = HorseColor.accent,
                    modifier = Modifier.size(CustomSize.toolbarActionIconSize)
                )
            }
        }
        Text(
            modifier = Modifier.padding(top = Padding.small),
            text = label,
            style = TextStyle(color = HorseColor.textSecondary, fontSize = FontSize.ultraSmall)
        )
    }
}

@Preview
@Composable
private fun QuickActionsContentPreview() {
    HorseTheme {
        QuickActionsContent(
            onClickPlantel = {},
            onClickVacinas = {},
            onClickCruzamentos = {},
            onClickFinancas = {}
        )
    }
}
