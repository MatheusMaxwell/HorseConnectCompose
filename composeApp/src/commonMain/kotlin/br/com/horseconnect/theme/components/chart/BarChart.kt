package br.com.horseconnect.theme.components.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import br.com.horseconnect.impl.domain.model.AnimalType
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.dimens.padding.Padding
import org.jetbrains.compose.ui.tooling.preview.Preview

data class BarData(val name: String, val value: Float)

@Composable
fun BarChart(
    data: List<BarData>,
    modifier: Modifier = Modifier
) {
    val maxValue = AnimalType.entries.size
    val maxHeight = 300f // Definindo uma altura máxima fixa para o gráfico
    // Criando o TextMeasurer
    val textMeasurer: TextMeasurer = rememberTextMeasurer()

    Column {
        Canvas(modifier = modifier.fillMaxWidth().height(200.dp).padding(start = Padding.small)) {
            val barWidth = size.width / maxValue
            val barSpacing = 12f

            // Estilo para o texto
            val textStyle = TextStyle(
                color = Color.Black, // Cor do texto
                fontSize = 12.sp, // Tamanho da fonte
                fontFamily = FontFamily.Default // Família da fonte
            )

            // Desenhando as barras
            data.forEachIndexed { index, bar ->
                val left = barWidth * index + barSpacing
                val right = left + barWidth - barSpacing

                // Garantindo que a posição "top" não fique negativa
                val top = maxOf(0f, size.height - (bar.value / maxValue * size.height + 1))
                val bottom = size.height
                val barWidth = size.width / maxValue - barSpacing * maxValue

                drawRoundRect(
                    color = HorseColor.primary,
                    topLeft = androidx.compose.ui.geometry.Offset(left, top),
                    size = androidx.compose.ui.geometry.Size(barWidth, bottom - top),
                    cornerRadius = CornerRadius(20f),
                    style = androidx.compose.ui.graphics.drawscope.Fill
                )
            }

            // Adicionando os valores no eixo Y
            data.forEachIndexed { index, bar ->
                val text = bar.value.toInt().toString()
                val textLayoutResult = textMeasurer.measure(text, style = textStyle)

                // Garantindo que a posição "top" para o texto dos valores não ultrapasse o limite superior
                val offsetX = barWidth * index + barSpacing * 4
                val offsetY = maxOf(0f, size.height - (bar.value / maxValue * size.height)) - 50

                // Desenhando o valor
                drawText(
                    textMeasurer = textMeasurer,
                    text = text,
                    topLeft = Offset(offsetX, offsetY),
                    style = textStyle,
                    overflow = TextOverflow.Clip,
                    softWrap = true
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            data.forEachIndexed { index, barData ->
                Text(
                    modifier = Modifier,
                    text = barData.name,
                    fontSize = 10.sp
                )
            }
        }
    }


}

@Composable
fun BarChartScreen() {
    val data = listOf(
        BarData("A", 30f),
        BarData("B", 45f),
        BarData("C", 80f),
        BarData("D", 60f),
        BarData("E", 90f),
        BarData("F", 70f),
        BarData("G", 50f)
    )

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Gráfico de Barras", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))
        BarChart(data = data)
    }
}

@Preview
@Composable
fun PreviewBarChart() {
    BarChartScreen()
}
