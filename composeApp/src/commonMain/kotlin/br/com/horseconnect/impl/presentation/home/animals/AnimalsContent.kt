package br.com.horseconnect.impl.presentation.home.animals

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.AnimalType
import br.com.horseconnect.impl.presentation.home.title.HomeTitle
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.chart.BarChart
import br.com.horseconnect.theme.components.chart.BarData
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.size.CustomSize
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AnimalsContent(
    animals: List<Animal>,
    onClickAnimals: () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(Padding.medium),
    ) {
        HomeTitle("Plantel"){
            onClickAnimals.invoke()
        }
        Card(
            elevation = CustomSize.ultraSmall,
            backgroundColor = Color.White,
            shape = MaterialTheme.shapes.small
        ){
            if (animals.isNotEmpty()) {
                Chart(animals)
            }
        }
    }
}

@Composable
private fun Chart(animals: List<Animal>) {
    Box(
        modifier = Modifier.padding(Padding.medium)
    ) {
        BarChart(
            data = AnimalType.entries.map {
                BarData(
                    name = it.value,
                    value = animals.filter { animal -> animal.types.contains(it) }.size.toFloat()
                )
            }
        )
    }
}

@Preview
@Composable
private fun AnimalsContentPreview() {

}