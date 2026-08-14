package br.com.horseconnect.theme.components.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.dimens.fontsize.FontSize
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.size.CustomSize
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class BottomBarDestination(val label: String) {
    HOME("Início"),
    REPORTS("Relatórios"),
    PROFILE("Perfil")
}

@Composable
fun BottomBar(
    selected: BottomBarDestination,
    onSelect: (BottomBarDestination) -> Unit
) {
    Column(modifier = Modifier.background(HorseColor.background)) {
        Divider(color = HorseColor.border, thickness = CustomSize.borderThin)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Padding.medium),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BottomBarDestination.entries.forEach { destination ->
                BottomBarItem(
                    destination = destination,
                    isSelected = destination == selected,
                    onClick = { onSelect(destination) }
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    destination: BottomBarDestination,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val tint = if (isSelected) HorseColor.accent else HorseColor.textSecondary
    Column(
        modifier = Modifier.clickable { onClick.invoke() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (destination) {
            BottomBarDestination.HOME -> Icon(
                imageVector = Icons.Default.Home,
                contentDescription = destination.label,
                tint = tint,
                modifier = Modifier.size(CustomSize.toolbarActionIconSize)
            )
            BottomBarDestination.REPORTS -> Icon(
                imageVector = Icons.Default.Assessment,
                contentDescription = destination.label,
                tint = tint,
                modifier = Modifier.size(CustomSize.toolbarActionIconSize)
            )
            BottomBarDestination.PROFILE -> Icon(
                imageVector = Icons.Default.Person,
                contentDescription = destination.label,
                tint = tint,
                modifier = Modifier.size(CustomSize.toolbarActionIconSize)
            )
        }
        Text(
            text = destination.label,
            style = TextStyle(color = tint, fontSize = FontSize.ultraSmall)
        )
    }
}

@Preview
@Composable
private fun BottomBarPreview() {
    HorseTheme {
        BottomBar(selected = BottomBarDestination.HOME, onSelect = {})
    }
}
