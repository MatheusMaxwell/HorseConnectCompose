package br.com.horseconnect.impl.presentation.home.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.dimens.fontsize.FontSize
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.size.CustomSize
import horseconnect.composeapp.generated.resources.Res
import horseconnect.composeapp.generated.resources.baseline_more_vert_24
import horseconnect.composeapp.generated.resources.icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Toolbar(farmName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(HorseColor.primary)
            .padding(Padding.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Icon(
            modifier = Modifier.size(CustomSize.toolbarIconSize),
            painter = painterResource(Res.drawable.icon),
            contentDescription = "",
            tint = Color.White
        )
        Text(
            text = farmName,
            style = TextStyle(color = Color.White, fontSize = FontSize.medium)
        )
        Box(
            modifier = Modifier.size(CustomSize.toolbarIconSize),
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

@Preview
@Composable
private fun ToolbarPreview() {
    Toolbar("Haras Teste")
}