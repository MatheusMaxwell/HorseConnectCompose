package br.com.horseconnect.theme.components.titlevalue

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import br.com.horseconnect.theme.dimens.padding.Padding

@Composable
fun TitleValue(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(top = Padding.small)
    ) {
        Text("$title:", style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold))
        Text(" $value", style = MaterialTheme.typography.h6, color = Color.Gray)
    }
}