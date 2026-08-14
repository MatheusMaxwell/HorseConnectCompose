package br.com.horseconnect.impl.presentation.vaccines.list.content

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
import androidx.compose.material.Icon
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
import androidx.compose.ui.unit.dp
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.common.viewstate.BaseViewState
import br.com.horseconnect.impl.domain.model.Vaccine
import br.com.horseconnect.impl.presentation.vaccines.VaccineOptions
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.loading.LoadingDialogManager
import br.com.horseconnect.theme.components.searchbar.SearchBar
import br.com.horseconnect.theme.components.toolbar.Toolbar
import br.com.horseconnect.theme.dimens.fontsize.FontSize
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.shape.Shape
import br.com.horseconnect.theme.dimens.size.CustomSize
import horseconnect.composeapp.generated.resources.Res
import horseconnect.composeapp.generated.resources.baseline_health_24
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun VaccineListContent(
    viewState: BaseViewState<List<Vaccine>>,
    loadingDialogManager: LoadingDialogManager,
    onSearchQueryChanged: (String) -> Unit,
    onNameFilterChanged: (String?) -> Unit,
    onGroupClick: (vaccines: List<Vaccine>) -> Unit,
    popBackStack: () -> Unit,
    navigateToRegister: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedName by remember { mutableStateOf<String?>(null) }
    var filterExpanded by remember { mutableStateOf(false) }
    var vaccines: List<Vaccine> by remember { mutableStateOf(emptyList()) }
    val toaster = LocalToasterDialog.current

    LaunchedEffect(viewState) {
        when(viewState) {
            BaseViewState.Initial -> Unit
            BaseViewState.Loading -> {
                loadingDialogManager.show()
            }
            is BaseViewState.Populated -> {
                loadingDialogManager.hide()
                vaccines = viewState.data
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
                title = "Vacinas",
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
                        selectedName = null
                        onNameFilterChanged(null)
                        filterExpanded = false
                    }) {
                        Text("Todos", color = HorseColor.textPrimary)
                    }
                    VaccineOptions.names.forEach { name ->
                        DropdownMenuItem(onClick = {
                            selectedName = name
                            onNameFilterChanged(name)
                            filterExpanded = false
                        }) {
                            Text(name, color = HorseColor.textPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Padding.large))

            val filteredVaccines = vaccines.filter { vaccine ->
                (searchQuery.isEmpty() ||
                        vaccine.animalName.contains(searchQuery, ignoreCase = true) ||
                        vaccine.name.contains(searchQuery, ignoreCase = true)) &&
                        (selectedName == null || vaccine.name == selectedName)
            }

            val groupedVaccines = filteredVaccines.groupBy {
                VaccineGroupKey(
                    name = it.name,
                    applicationDate = it.applicationDate,
                    nextDueDate = it.nextDueDate,
                    veterinarian = it.veterinarian,
                    batch = it.batch
                )
            }.values.toList()

            Text(
                text = "${groupedVaccines.size} vacinas",
                color = HorseColor.textSecondary,
                style = TextStyle(fontSize = FontSize.ultraSmall)
            )

            Spacer(modifier = Modifier.height(Padding.small))

            LazyColumn {
                items(groupedVaccines) { group ->
                    VaccineGroupListItem(group) {
                        onGroupClick.invoke(it)
                    }
                }
            }
        }
    }
}

private data class VaccineGroupKey(
    val name: String,
    val applicationDate: String,
    val nextDueDate: String,
    val veterinarian: String,
    val batch: String
)

@Composable
private fun VaccineGroupListItem(vaccines: List<Vaccine>, onGroupClick: (vaccines: List<Vaccine>) -> Unit) {
    val vaccine = vaccines.first()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Padding.small)
            .clickable { onGroupClick.invoke(vaccines) },
        elevation = 0.dp,
        shape = Shape.medium,
        backgroundColor = HorseColor.surface,
        border = BorderStroke(CustomSize.borderThin, HorseColor.border)
    ) {
        Row(
            modifier = Modifier.padding(Padding.large),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(CustomSize.animalCardImageSize)
                    .clip(Shape.small)
                    .background(HorseColor.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.baseline_health_24),
                    contentDescription = "",
                    tint = HorseColor.accent
                )
            }

            Spacer(modifier = Modifier.width(Padding.large))

            Column {
                Text(vaccine.name, color = HorseColor.textPrimary, style = MaterialTheme.typography.h6)
                Text(
                    if (vaccines.size > 1) "${vaccines.size} animais vacinados" else "1 animal vacinado",
                    color = HorseColor.textSecondary,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    "Aplicada em ${DateUtil.getDateFormatter(vaccine.applicationDate)}",
                    color = HorseColor.textSecondary,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    "Próxima dose: ${DateUtil.getDateFormatter(vaccine.nextDueDate)}",
                    color = HorseColor.accent,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Preview
@Composable
private fun VaccineListContentPreview() {
    HorseTheme {
        VaccineListContent(
            viewState = BaseViewState.Populated(listOf(Vaccine.mock, Vaccine.mock)),
            loadingDialogManager = LoadingDialogManager(),
            onSearchQueryChanged = {},
            onNameFilterChanged = {},
            onGroupClick = {},
            popBackStack = {},
            navigateToRegister = {}
        )
    }
}
