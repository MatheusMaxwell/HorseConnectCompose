package br.com.horseconnect.theme.components.expandablelist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.titlevalue.TitleValue
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.shape.Shape
import br.com.horseconnect.theme.dimens.size.CustomSize
import org.jetbrains.compose.ui.tooling.preview.Preview

// Data class to represent an item in the list
data class Item(val name: String, val value: String)

@Composable
fun ExpandableList(
    title: String,
    items: List<Item>,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        backgroundColor = HorseColor.surface,
        shape = Shape.medium,
        border = BorderStroke(CustomSize.borderThin, HorseColor.border),
        elevation = 0.dp
    ) {
        Column {
            // Title section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(Padding.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = HorseColor.textPrimary,
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = if (isExpanded) "-" else "+",
                    color = HorseColor.accent,
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
            }

            // Animated content (list of items)
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(300))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Padding.large)
                ) {
                    items.forEach { item ->
                        TitleValue(
                            title = item.name,
                            value = item.value
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ExpandableListPreview() {
    HorseTheme {
        ExpandableList(
            title = "Outros proprietários",
            items = listOf(Item("João", "50%"), Item("Maria", "25%"))
        )
    }
}
