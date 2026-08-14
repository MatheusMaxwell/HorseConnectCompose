package br.com.horseconnect.theme.components.searchbar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.dimens.fontsize.FontSize
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.shape.Shape
import br.com.horseconnect.theme.dimens.size.CustomSize
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onFilterClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(CustomSize.searchBarHeight)
            .padding(vertical = Padding.small),
        elevation = 0.dp,
        shape = Shape.medium,
        backgroundColor = HorseColor.surface,
        border = BorderStroke(CustomSize.borderThin, HorseColor.border)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = Padding.large),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(CustomSize.toolbarActionIconSize),
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = HorseColor.textSecondary
            )

            Spacer(modifier = Modifier.width(Padding.small))

            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        text = "Buscar",
                        color = HorseColor.textMuted,
                        style = TextStyle(fontSize = FontSize.ultraSmall)
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChanged,
                    singleLine = true,
                    textStyle = TextStyle(color = HorseColor.textPrimary, fontSize = FontSize.ultraSmall),
                    cursorBrush = SolidColor(HorseColor.accent),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (onFilterClick != null) {
                Spacer(modifier = Modifier.width(Padding.small))

                Icon(
                    modifier = Modifier
                        .size(CustomSize.toolbarActionIconSize)
                        .clickable { onFilterClick.invoke() },
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filtrar",
                    tint = HorseColor.textSecondary
                )
            }
        }
    }
}

@Preview
@Composable
private fun SearchBarPreview() {
    HorseTheme {
        SearchBar(query = "", onQueryChanged = {}, onFilterClick = {})
    }
}
