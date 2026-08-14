package br.com.horseconnect.impl.presentation.home.stats

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.horseconnect.common.ext.toCurrencyString
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.dimens.fontsize.FontSize
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.shape.Shape
import br.com.horseconnect.theme.dimens.size.CustomSize
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeStatsContent(
    plantelCount: Int,
    pregnanciesCount: Int,
    upcomingVaccinesCount: Int?,
    monthBalance: Double?,
    onClickPlantel: () -> Unit,
    onClickGestacoes: () -> Unit,
    onClickVacinas: () -> Unit,
    onClickFinancas: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(Padding.medium)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Padding.medium)
        ) {
            StatTile(
                modifier = Modifier.weight(1f),
                label = "Plantel",
                value = plantelCount.toString(),
                valueColor = HorseColor.textPrimary,
                onClick = onClickPlantel
            )
            StatTile(
                modifier = Modifier.weight(1f),
                label = "Vacinas próx.",
                value = upcomingVaccinesCount?.toString() ?: "—",
                valueColor = if (upcomingVaccinesCount != null) HorseColor.textPrimary else HorseColor.textMuted,
                onClick = onClickVacinas
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = Padding.medium),
            horizontalArrangement = Arrangement.spacedBy(Padding.medium)
        ) {
            StatTile(
                modifier = Modifier.weight(1f),
                label = "Gestações",
                value = pregnanciesCount.toString(),
                valueColor = HorseColor.textPrimary,
                onClick = onClickGestacoes
            )
            StatTile(
                modifier = Modifier.weight(1f),
                label = "Saldo do mês",
                value = monthBalance?.toCurrencyString() ?: "—",
                valueColor = when {
                    monthBalance == null -> HorseColor.textMuted
                    monthBalance >= 0 -> HorseColor.success
                    else -> HorseColor.error
                },
                onClick = onClickFinancas
            )
        }
    }
}

@Composable
private fun StatTile(
    modifier: Modifier,
    label: String,
    value: String,
    valueColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick.invoke() },
        backgroundColor = HorseColor.surface,
        shape = Shape.medium,
        border = BorderStroke(CustomSize.borderThin, HorseColor.border),
        elevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(Padding.large)) {
            Text(
                text = label,
                style = TextStyle(color = HorseColor.textSecondary, fontSize = FontSize.ultraSmall)
            )
            Text(
                modifier = Modifier.padding(top = Padding.small),
                text = value,
                style = TextStyle(color = valueColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview
@Composable
private fun HomeStatsContentPreview() {
    HorseTheme {
        HomeStatsContent(
            plantelCount = 24,
            pregnanciesCount = 2,
            upcomingVaccinesCount = 3,
            monthBalance = 8420.0,
            onClickPlantel = {},
            onClickGestacoes = {},
            onClickVacinas = {},
            onClickFinancas = {}
        )
    }
}
