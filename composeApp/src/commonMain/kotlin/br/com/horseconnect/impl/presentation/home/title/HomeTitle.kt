package br.com.horseconnect.impl.presentation.home.title

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.horseconnect.theme.dimens.fontsize.FontSize
import br.com.horseconnect.theme.dimens.padding.Padding
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeTitle(
    title: String,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
            .clickable { onClick.invoke() },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, style = TextStyle(fontSize = FontSize.small, fontWeight = FontWeight.Bold))
        Text(text = "Ver mais", style = TextStyle(fontSize = FontSize.ultraSmall))
    }
}

@Preview
@Composable
private fun HomeTitlePreview() {
    HomeTitle("Title"){}
}