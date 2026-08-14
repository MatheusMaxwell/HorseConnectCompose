package br.com.horseconnect.impl.presentation.pregnancies.list.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.common.viewstate.BaseViewState
import br.com.horseconnect.impl.domain.model.Pregnancy
import br.com.horseconnect.impl.presentation.pregnancies.PregnancyOptions
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
fun PregnancyListContent(
    viewState: BaseViewState<List<Pregnancy>>,
    loadingDialogManager: LoadingDialogManager,
    onSearchQueryChanged: (String) -> Unit,
    onStatusFilterChanged: (String?) -> Unit,
    onItemClick: (pregnancy: Pregnancy) -> Unit,
    popBackStack: () -> Unit,
    navigateToRegister: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<String?>(null) }
    var filterExpanded by remember { mutableStateOf(false) }
    var pregnancies: List<Pregnancy> by remember { mutableStateOf(emptyList()) }
    val toaster = LocalToasterDialog.current

    LaunchedEffect(viewState) {
        when(viewState) {
            BaseViewState.Initial -> Unit
            BaseViewState.Loading -> {
                loadingDialogManager.show()
            }
            is BaseViewState.Populated -> {
                loadingDialogManager.hide()
                pregnancies = viewState.data
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
                title = "Cruzamentos",
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
                        selectedStatus = null
                        onStatusFilterChanged(null)
                        filterExpanded = false
                    }) {
                        Text("Todos", color = HorseColor.textPrimary)
                    }
                    PregnancyOptions.statuses.forEach { status ->
                        DropdownMenuItem(onClick = {
                            selectedStatus = status
                            onStatusFilterChanged(status)
                            filterExpanded = false
                        }) {
                            Text(status, color = HorseColor.textPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Padding.large))

            val filteredPregnancies = pregnancies.filter { pregnancy ->
                (searchQuery.isEmpty() ||
                        pregnancy.femaleName.contains(searchQuery, ignoreCase = true) ||
                        pregnancy.maleName.contains(searchQuery, ignoreCase = true)) &&
                        (selectedStatus == null || pregnancy.status == selectedStatus)
            }

            Text(
                text = "${filteredPregnancies.size} cruzamentos",
                color = HorseColor.textSecondary,
                style = TextStyle(fontSize = FontSize.ultraSmall)
            )

            Spacer(modifier = Modifier.height(Padding.small))

            LazyColumn {
                items(filteredPregnancies) { pregnancy ->
                    PregnancyListItem(pregnancy) {
                        onItemClick.invoke(it)
                    }
                }
            }
        }
    }
}

@Composable
private fun PregnancyListItem(pregnancy: Pregnancy, onItemClick: (pregnancy: Pregnancy) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Padding.small)
            .clickable { onItemClick.invoke(pregnancy) },
        elevation = 0.dp,
        shape = Shape.medium,
        backgroundColor = HorseColor.surface,
        border = BorderStroke(CustomSize.borderThin, HorseColor.border)
    ) {
        Row(modifier = Modifier.padding(Padding.large)) {

            Column {
                Text(
                    "${pregnancy.maleName} x ${pregnancy.femaleName}",
                    color = HorseColor.textPrimary,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    pregnancy.status,
                    color = HorseColor.accent,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    "Previsão: ${DateUtil.calculateBirthForecast(pregnancy.date)}",
                    color = HorseColor.textSecondary,
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}

@Preview
@Composable
private fun PregnancyListContentPreview() {
    HorseTheme {
        PregnancyListContent(
            viewState = BaseViewState.Populated(listOf(Pregnancy.mock, Pregnancy.mock)),
            loadingDialogManager = LoadingDialogManager(),
            onSearchQueryChanged = {},
            onStatusFilterChanged = {},
            onItemClick = {},
            popBackStack = {},
            navigateToRegister = {}
        )
    }
}
