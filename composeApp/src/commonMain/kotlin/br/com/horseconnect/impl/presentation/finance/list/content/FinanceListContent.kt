package br.com.horseconnect.impl.presentation.finance.list.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.horseconnect.common.ext.toCurrencyString
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.common.viewstate.BaseViewState
import br.com.horseconnect.impl.domain.model.FinanceEntry
import br.com.horseconnect.impl.domain.model.FinanceType
import br.com.horseconnect.impl.domain.util.balance
import br.com.horseconnect.impl.domain.util.toCashEvents
import br.com.horseconnect.impl.presentation.finance.FinanceOptions
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.loading.LoadingDialogManager
import br.com.horseconnect.theme.components.searchbar.SearchBar
import br.com.horseconnect.theme.components.toolbar.Toolbar
import br.com.horseconnect.theme.dimens.fontsize.FontSize
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.shape.Shape
import br.com.horseconnect.theme.dimens.size.CustomSize
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FinanceListContent(
    viewState: BaseViewState<List<FinanceEntry>>,
    loadingDialogManager: LoadingDialogManager,
    onSearchQueryChanged: (String) -> Unit,
    onCategoryFilterChanged: (String?) -> Unit,
    onItemClick: (entry: FinanceEntry) -> Unit,
    popBackStack: () -> Unit,
    navigateToRegister: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var filterExpanded by remember { mutableStateOf(false) }
    var entries: List<FinanceEntry> by remember { mutableStateOf(emptyList()) }
    val toaster = LocalToasterDialog.current

    LaunchedEffect(viewState) {
        when(viewState) {
            BaseViewState.Initial -> Unit
            BaseViewState.Loading -> {
                loadingDialogManager.show()
            }
            is BaseViewState.Populated -> {
                loadingDialogManager.hide()
                entries = viewState.data
            }
            is BaseViewState.Error -> {
                loadingDialogManager.hide()
                toaster.show(viewState.msg)
            }
        }
    }

    Scaffold(
        topBar = {
            Toolbar(
                title = "Finanças",
                onNavigationAction = {
                    popBackStack.invoke()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigateToRegister.invoke()
                },
                backgroundColor = HorseColor.accent,
                contentColor = HorseColor.onAccent
            ) {
                Text("+", style = MaterialTheme.typography.h6)
            }
        },
        backgroundColor = HorseColor.background
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(Padding.large)) {
            BalanceCard(entries)

            Spacer(modifier = Modifier.height(Padding.large))

            Box(modifier = Modifier.fillMaxWidth()) {
                SearchBar(
                    query = searchQuery,
                    onQueryChanged = {
                        searchQuery = it
                        onSearchQueryChanged(it)
                    },
                    onFilterClick = { filterExpanded = !filterExpanded }
                )

                DropdownMenu(
                    expanded = filterExpanded,
                    onDismissRequest = { filterExpanded = false }
                ) {
                    DropdownMenuItem(onClick = {
                        selectedCategory = null
                        onCategoryFilterChanged(null)
                        filterExpanded = false
                    }) {
                        Text("Todos", color = HorseColor.textPrimary)
                    }
                    (FinanceOptions.incomeCategories + FinanceOptions.expenseCategories).distinct().forEach { category ->
                        DropdownMenuItem(onClick = {
                            selectedCategory = category
                            onCategoryFilterChanged(category)
                            filterExpanded = false
                        }) {
                            Text(category, color = HorseColor.textPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Padding.large))

            val filteredEntries = entries.filter { entry ->
                (searchQuery.isEmpty() ||
                        entry.description.contains(searchQuery, ignoreCase = true) ||
                        entry.category.contains(searchQuery, ignoreCase = true)) &&
                        (selectedCategory == null || entry.category == selectedCategory)
            }

            Text(
                text = "${filteredEntries.size} lançamentos",
                color = HorseColor.textSecondary,
                style = TextStyle(fontSize = FontSize.ultraSmall)
            )

            Spacer(modifier = Modifier.height(Padding.small))

            LazyColumn {
                items(filteredEntries) { entry ->
                    FinanceEntryListItem(entry) {
                        onItemClick.invoke(it)
                    }
                }
            }
        }
    }
}

@Composable
private fun BalanceCard(entries: List<FinanceEntry>) {
    var showTotal by remember { mutableStateOf(false) }
    val cashEvents = entries.toCashEvents()
    val balance = if (showTotal) {
        cashEvents.filter { DateUtil.isPastOrToday(it.date) }.balance()
    } else {
        cashEvents.filter { DateUtil.isCurrentMonth(it.date) }.balance()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 0.dp,
        shape = Shape.medium,
        backgroundColor = HorseColor.surface,
        border = BorderStroke(CustomSize.borderThin, HorseColor.border)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(Padding.large)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (showTotal) "Saldo total" else "Saldo do mês",
                    color = HorseColor.textSecondary,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    if (showTotal) "Ver saldo do mês" else "Ver saldo total",
                    modifier = Modifier.clickable { showTotal = !showTotal },
                    color = HorseColor.accent,
                    style = MaterialTheme.typography.caption
                )
            }
            Text(
                balance.toCurrencyString(),
                color = if (balance >= 0) HorseColor.success else HorseColor.error,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun FinanceEntryListItem(entry: FinanceEntry, onItemClick: (entry: FinanceEntry) -> Unit) {
    val isIncome = entry.type == FinanceType.INCOME
    val amountColor = if (isIncome) HorseColor.success else HorseColor.error
    val amountPrefix = if (isIncome) "+ " else "- "

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Padding.small)
            .clickable { onItemClick.invoke(entry) },
        elevation = 0.dp,
        shape = Shape.medium,
        backgroundColor = HorseColor.surface,
        border = BorderStroke(CustomSize.borderThin, HorseColor.border)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(Padding.large),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(entry.description, color = HorseColor.textPrimary, style = MaterialTheme.typography.h6)
                Text(
                    entry.category,
                    color = HorseColor.textSecondary,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    DateUtil.getDateFormatter(entry.date),
                    color = HorseColor.textSecondary,
                    style = MaterialTheme.typography.body2
                )
                if (entry.isRecurring) {
                    Text(
                        "Recorrente",
                        color = HorseColor.accent,
                        style = MaterialTheme.typography.caption
                    )
                } else if (entry.isInstallment) {
                    Text(
                        "Parcelado em ${entry.installments.size}x",
                        color = HorseColor.accent,
                        style = MaterialTheme.typography.caption
                    )
                }
            }

            Text(
                "$amountPrefix${entry.amount.toCurrencyString()}",
                color = amountColor,
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview
@Composable
private fun FinanceListContentPreview() {
    HorseTheme {
        FinanceListContent(
            viewState = BaseViewState.Populated(listOf(FinanceEntry.mock, FinanceEntry.mock)),
            loadingDialogManager = LoadingDialogManager(),
            onSearchQueryChanged = {},
            onCategoryFilterChanged = {},
            onItemClick = {},
            popBackStack = {},
            navigateToRegister = {}
        )
    }
}
