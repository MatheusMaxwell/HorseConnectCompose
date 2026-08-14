package br.com.horseconnect.impl.presentation.home.pregnancies

import androidx.compose.runtime.Composable
import br.com.horseconnect.impl.domain.model.Pregnancy
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.impl.presentation.home.title.HomeTitle
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.dimens.fontsize.FontSize
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.shape.Shape
import br.com.horseconnect.theme.dimens.size.CustomSize

@Composable
fun PregnanciesContent(
    pregnancies: List<Pregnancy>,
    onClickPregnancies: () -> Unit,
    onClickPregnancy: (Pregnancy) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(Padding.medium)
    ) {
        HomeTitle("Próximos eventos"){
            onClickPregnancies.invoke()
        }

        if (pregnancies.isEmpty()) {
            Text(
                modifier = Modifier.padding(top = Padding.medium),
                text = "Nenhum evento por vir.",
                style = TextStyle(color = HorseColor.textMuted, fontSize = FontSize.ultraSmall)
            )
        }

        Column(modifier = Modifier.padding(top = Padding.small)) {
            pregnancies.forEach { pregnancy ->
                PregnancyRow(pregnancy) { onClickPregnancy.invoke(pregnancy) }
                Spacer(modifier = Modifier.height(Padding.small))
            }
        }
    }
}

@Composable
private fun PregnancyRow(pregnancy: Pregnancy, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick.invoke() },
        elevation = 0.dp,
        backgroundColor = HorseColor.surface,
        shape = Shape.medium,
        border = BorderStroke(CustomSize.borderThin, HorseColor.border)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(Padding.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(CustomSize.animalCardImageSize / 2)
                    .clip(Shape.small)
                    .background(HorseColor.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "",
                    tint = HorseColor.accent
                )
            }

            Column(
                modifier = Modifier.weight(1f).padding(horizontal = Padding.medium)
            ) {
                Text(
                    text = "${pregnancy.maleName} x ${pregnancy.femaleName}",
                    style = TextStyle(color = HorseColor.textPrimary, fontWeight = FontWeight.Bold, fontSize = FontSize.ultraSmall)
                )
                Text(
                    text = "Previsão: ${DateUtil.calculateBirthForecast(pregnancy.date)}",
                    style = TextStyle(color = HorseColor.textSecondary, fontSize = FontSize.ultraSmall)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "",
                tint = HorseColor.textMuted
            )
        }
    }
}

@Preview
@Composable
private fun PregnanciesContentPreview() {
    HorseTheme {
        PregnanciesContent(
            pregnancies = listOf(
                Pregnancy.mock,
                Pregnancy.mock
            ),
            onClickPregnancies = {}
        )
    }
}
