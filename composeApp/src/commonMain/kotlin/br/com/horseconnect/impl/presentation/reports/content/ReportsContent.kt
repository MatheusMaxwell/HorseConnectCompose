package br.com.horseconnect.impl.presentation.reports.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.horseconnect.common.ext.toCurrencyString
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.common.viewstate.BaseViewState
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.AnimalType
import br.com.horseconnect.impl.domain.model.FinanceEntry
import br.com.horseconnect.impl.domain.model.FinanceType
import br.com.horseconnect.impl.domain.model.Pregnancy
import br.com.horseconnect.impl.domain.model.Vaccine
import br.com.horseconnect.impl.domain.util.balance
import br.com.horseconnect.impl.domain.util.toCashEvents
import br.com.horseconnect.impl.presentation.pregnancies.PregnancyOptions
import br.com.horseconnect.impl.presentation.reports.ReportsData
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.loading.LoadingDialogManager
import br.com.horseconnect.theme.components.toolbar.Toolbar
import br.com.horseconnect.theme.dimens.fontsize.FontSize
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.shape.Shape
import br.com.horseconnect.theme.dimens.size.CustomSize
import horseconnect.composeapp.generated.resources.Res
import horseconnect.composeapp.generated.resources.baseline_health_24
import horseconnect.composeapp.generated.resources.baseline_wallet_24
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private data class ChartSegment(val label: String, val value: Float, val color: Color)

@Composable
fun ReportsContent(
    viewState: BaseViewState<ReportsData>,
    loadingDialogManager: LoadingDialogManager
) {
    val toaster = LocalToasterDialog.current

    LaunchedEffect(viewState) {
        when(viewState) {
            BaseViewState.Initial -> Unit
            BaseViewState.Loading -> loadingDialogManager.show()
            is BaseViewState.Populated -> loadingDialogManager.hide()
            is BaseViewState.Error -> {
                loadingDialogManager.hide()
                toaster.show(viewState.msg)
            }
        }
    }

    Scaffold(
        topBar = {
            Toolbar(title = "Relatórios")
        },
        backgroundColor = HorseColor.background
    ) {
        val data = (viewState as? BaseViewState.Populated)?.data
        if (data != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(Padding.large),
                verticalArrangement = Arrangement.spacedBy(Padding.large)
            ) {
                PlantelSection(data.animals)
                CruzamentosSection(data.pregnancies)
                VacinasSection(data.vaccines)
                FinanceiroSection(data.financeEntries)
            }
        }
    }
}

@Composable
private fun ReportSection(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 0.dp,
        shape = Shape.medium,
        backgroundColor = HorseColor.surface,
        border = BorderStroke(CustomSize.borderThin, HorseColor.border)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(Padding.large), content = content)
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: @Composable () -> Unit,
    trailing: @Composable () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon()
            Spacer(modifier = Modifier.width(Padding.small))
            Text(
                title,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                color = HorseColor.textPrimary
            )
        }
        trailing()
    }
}

@Composable
private fun StatusBadge(text: String, color: Color) {
    Text(
        text,
        color = color,
        style = TextStyle(fontSize = FontSize.ultraSmall, fontWeight = FontWeight.Bold),
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), CircleShape)
            .padding(horizontal = Padding.medium, vertical = Padding.small)
    )
}

@Composable
private fun ChartLegendItem(segment: ChartSegment, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(CustomSize.chartLegendDotSize).background(segment.color, CircleShape))
        Spacer(modifier = Modifier.width(Padding.small))
        Text(
            "${segment.label} — ${segment.value.toInt()}",
            style = TextStyle(fontSize = FontSize.ultraSmall),
            color = HorseColor.textPrimary
        )
    }
}

@Composable
private fun ChartLegendGrid(segments: List<ChartSegment>) {
    Column(verticalArrangement = Arrangement.spacedBy(Padding.small)) {
        segments.chunked(2).forEach { rowSegments ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                rowSegments.forEach { segment ->
                    ChartLegendItem(segment = segment, modifier = Modifier.weight(1f))
                }
                if (rowSegments.size == 1) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun DonutChart(segments: List<ChartSegment>, centerValue: String, centerLabel: String) {
    val total = segments.sumOf { it.value.toDouble() }.toFloat().coerceAtLeast(1f)
    Box(modifier = Modifier.size(CustomSize.donutChartSize), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidthPx = CustomSize.donutChartStroke.toPx()
            var startAngle = -90f
            segments.forEach { segment ->
                if (segment.value > 0f) {
                    val sweep = 360f * (segment.value / total)
                    drawArc(
                        color = segment.color,
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = Stroke(width = strokeWidthPx),
                        topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
                        size = Size(size.width - strokeWidthPx, size.height - strokeWidthPx)
                    )
                    startAngle += sweep
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                centerValue,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                color = HorseColor.textPrimary
            )
            Text(centerLabel, style = TextStyle(fontSize = FontSize.ultraSmall), color = HorseColor.textSecondary)
        }
    }
}

@Composable
private fun SegmentedBar(segments: List<ChartSegment>, height: Dp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(HorseColor.border, CircleShape)
    ) {
        segments.filter { it.value > 0f }.forEach { segment ->
            Box(modifier = Modifier.weight(segment.value).fillMaxHeight().background(segment.color))
        }
    }
}

@Composable
private fun LabeledBar(label: String, value: Int, maxValue: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = TextStyle(fontSize = FontSize.ultraSmall), color = HorseColor.textPrimary)
            Text("$value", style = TextStyle(fontSize = FontSize.ultraSmall), color = HorseColor.textSecondary)
        }
        Spacer(modifier = Modifier.height(Padding.small))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(CustomSize.labeledBarHeight)
                .background(HorseColor.border, CircleShape)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = (value.toFloat() / maxValue.toFloat()).coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .background(HorseColor.accent, CircleShape)
            )
        }
    }
}

@Composable
private fun LegendAmount(label: String, amount: Double, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(CustomSize.chartLegendDotSize).background(color, CircleShape))
        Spacer(modifier = Modifier.width(Padding.small))
        Text(
            "$label ${amount.toCurrencyString()}",
            style = TextStyle(fontSize = FontSize.ultraSmall),
            color = HorseColor.textPrimary
        )
    }
}

@Composable
private fun PlantelSection(animals: List<Animal>) {
    ReportSection {
        SectionHeader(
            title = "Plantel",
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = null,
                    tint = HorseColor.accent,
                    modifier = Modifier.size(CustomSize.toolbarActionIconSize)
                )
            },
            trailing = {
                StatusBadge(text = "${animals.count { it.isLive }} vivos", color = HorseColor.success)
            }
        )
        Spacer(modifier = Modifier.height(Padding.large))

        val counts = AnimalType.entries
            .map { type -> type.value to animals.count { type in it.types } }
            .sortedByDescending { it.second }
        val top = counts.take(4).filter { it.second > 0 }
        val othersTotal = counts.drop(4).sumOf { it.second }
        val palette = listOf(HorseColor.accent, HorseColor.accentLight, HorseColor.accentDeep, HorseColor.textSecondary)
        val segments = top.mapIndexed { index, (label, count) ->
            ChartSegment(label, count.toFloat(), palette.getOrElse(index) { HorseColor.chartMuted })
        } + if (othersTotal > 0) listOf(ChartSegment("Outros", othersTotal.toFloat(), HorseColor.chartMuted)) else emptyList()

        Row(verticalAlignment = Alignment.CenterVertically) {
            DonutChart(segments = segments, centerValue = "${animals.size}", centerLabel = "animais")
            Spacer(modifier = Modifier.width(Padding.x2Large))
            Column(verticalArrangement = Arrangement.spacedBy(Padding.small)) {
                segments.forEach { ChartLegendItem(segment = it) }
            }
        }
    }
}

@Composable
private fun CruzamentosSection(pregnancies: List<Pregnancy>) {
    ReportSection {
        SectionHeader(
            title = "Cruzamentos",
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = HorseColor.accent,
                    modifier = Modifier.size(CustomSize.toolbarActionIconSize)
                )
            }
        )
        Spacer(modifier = Modifier.height(Padding.small))

        val active = pregnancies.count { it.status !in listOf("Finalizado", "Perdido") }
        Text(
            "${pregnancies.size} no total · $active ativas",
            style = TextStyle(fontSize = FontSize.ultraSmall),
            color = HorseColor.textSecondary
        )
        Spacer(modifier = Modifier.height(Padding.large))

        val statusColors = mapOf(
            "Confirmado" to HorseColor.success,
            "A confirmar" to HorseColor.accent,
            "Não confirmado" to HorseColor.warning,
            "Perdido" to HorseColor.error,
            "Finalizado" to HorseColor.chartMuted
        )
        val segments = PregnancyOptions.statuses.mapNotNull { status ->
            val count = pregnancies.count { it.status == status }
            if (count > 0) ChartSegment(status, count.toFloat(), statusColors[status] ?: HorseColor.chartMuted) else null
        }

        SegmentedBar(segments = segments, height = CustomSize.segmentedBarHeight)
        Spacer(modifier = Modifier.height(Padding.medium))
        ChartLegendGrid(segments = segments)
    }
}

@Composable
private fun VacinasSection(vaccines: List<Vaccine>) {
    ReportSection {
        val boundary = DateUtil.addDays(DateUtil.now(), 30)
        val upcoming = vaccines.count { it.nextDueDate in DateUtil.now()..boundary }

        SectionHeader(
            title = "Vacinas",
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.baseline_health_24),
                    contentDescription = null,
                    tint = HorseColor.accent,
                    modifier = Modifier.size(CustomSize.toolbarActionIconSize)
                )
            },
            trailing = {
                StatusBadge(text = "$upcoming nos próx. 30 dias", color = HorseColor.warning)
            }
        )
        Spacer(modifier = Modifier.height(Padding.small))
        Text(
            "${vaccines.size} aplicações no total",
            style = TextStyle(fontSize = FontSize.ultraSmall),
            color = HorseColor.textSecondary
        )
        Spacer(modifier = Modifier.height(Padding.large))

        val topVaccines = vaccines.groupBy { it.name }
            .map { (name, list) -> name to list.size }
            .sortedByDescending { it.second }
            .take(4)
        val maxValue = topVaccines.maxOfOrNull { it.second } ?: 1

        Column(verticalArrangement = Arrangement.spacedBy(Padding.medium)) {
            topVaccines.forEach { (name, count) ->
                LabeledBar(label = name, value = count, maxValue = maxValue)
            }
        }
    }
}

@Composable
private fun FinanceiroSection(entries: List<FinanceEntry>) {
    ReportSection {
        SectionHeader(
            title = "Financeiro",
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.baseline_wallet_24),
                    contentDescription = null,
                    tint = HorseColor.accent,
                    modifier = Modifier.size(CustomSize.toolbarActionIconSize)
                )
            }
        )
        Spacer(modifier = Modifier.height(Padding.medium))

        val cashEvents = entries.toCashEvents()
        val currentMonthEvents = cashEvents.filter { DateUtil.isCurrentMonth(it.date) }
        val monthBalance = currentMonthEvents.balance()
        val income = currentMonthEvents.filter { it.type == FinanceType.INCOME }.sumOf { it.amount }
        val expense = currentMonthEvents.filter { it.type == FinanceType.EXPENSE }.sumOf { it.amount }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text("Saldo do mês", style = TextStyle(fontSize = FontSize.ultraSmall), color = HorseColor.textSecondary)
                Text(
                    monthBalance.toCurrencyString(),
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                    color = if (monthBalance >= 0) HorseColor.success else HorseColor.error
                )
            }
            Text("${entries.size} lançamentos", style = TextStyle(fontSize = FontSize.ultraSmall), color = HorseColor.textSecondary)
        }
        Spacer(modifier = Modifier.height(Padding.medium))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            LegendAmount(label = "Entradas", amount = income, color = HorseColor.success)
            LegendAmount(label = "Saídas", amount = expense, color = HorseColor.error)
        }
        Spacer(modifier = Modifier.height(Padding.small))
        SegmentedBar(
            segments = listOf(
                ChartSegment("Entradas", income.toFloat(), HorseColor.success),
                ChartSegment("Saídas", expense.toFloat(), HorseColor.error)
            ),
            height = CustomSize.segmentedBarHeight
        )
    }
}

@Preview
@Composable
private fun ReportsContentPreview() {
    HorseTheme {
        ReportsContent(
            viewState = BaseViewState.Populated(
                ReportsData(
                    animals = listOf(Animal.mock),
                    pregnancies = listOf(Pregnancy.mock),
                    vaccines = listOf(Vaccine.mock),
                    financeEntries = listOf(FinanceEntry.mock)
                )
            ),
            loadingDialogManager = LoadingDialogManager()
        )
    }
}
