package br.com.horseconnect.impl.presentation.home.title

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.dimens.fontsize.FontSize
import br.com.horseconnect.theme.dimens.padding.Padding
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeTitle(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Padding.medium)
            .clickable { onClick.invoke() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = TextStyle(color = HorseColor.textPrimary, fontSize = FontSize.small, fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Ver mais",
            style = TextStyle(color = HorseColor.accent, fontSize = FontSize.ultraSmall, fontWeight = FontWeight.Bold)
        )
    }
}

@Preview
@Composable
private fun HomeTitlePreview() {
    HorseTheme {
        HomeTitle("Title"){}
    }
}