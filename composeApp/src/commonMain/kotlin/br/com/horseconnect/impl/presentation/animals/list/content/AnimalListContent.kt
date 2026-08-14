package br.com.horseconnect.impl.presentation.animals.list.content

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.common.viewstate.BaseViewState
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.AnimalType
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.loading.LoadingDialogManager
import br.com.horseconnect.theme.components.searchbar.SearchBar
import br.com.horseconnect.theme.components.toolbar.Toolbar
import br.com.horseconnect.theme.dimens.fontsize.FontSize
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.shape.Shape
import br.com.horseconnect.theme.dimens.size.CustomSize
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AnimalListContent(
    viewState: BaseViewState<List<Animal>>,
    loadingDialogManager: LoadingDialogManager,
    onSearchQueryChanged: (String) -> Unit,
    onTypeFilterChanged: (AnimalType?) -> Unit,
    onItemClick: (animalId: String) -> Unit,
    popBackStack: () -> Unit,
    navigateToRegister: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<AnimalType?>(null) }
    var filterExpanded by remember { mutableStateOf(false) }
    var animals: List<Animal> by remember{ mutableStateOf(emptyList()) }
    val toaster = LocalToasterDialog.current

    LaunchedEffect(viewState) {
        when(viewState) {
            BaseViewState.Initial -> Unit
            BaseViewState.Loading -> {
                loadingDialogManager.show()
            }
            is BaseViewState.Populated -> {
                loadingDialogManager.hide()
                animals = viewState.data
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
                title = "Plantel",
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
                        selectedType = null
                        onTypeFilterChanged(null)
                        filterExpanded = false
                    }) {
                        Text("Todos", color = HorseColor.textPrimary)
                    }
                    AnimalType.entries.forEach { type ->
                        DropdownMenuItem(onClick = {
                            selectedType = type
                            onTypeFilterChanged(type)
                            filterExpanded = false
                        }) {
                            Text(type.value, color = HorseColor.textPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Padding.large))

            val filteredAnimals = animals.filter { animal ->
                (searchQuery.isEmpty() || animal.name.contains(searchQuery, ignoreCase = true)) &&
                        (selectedType == null || animal.types.contains(selectedType))
            }

            Text(
                text = "${filteredAnimals.size} animais",
                color = HorseColor.textSecondary,
                style = TextStyle(fontSize = FontSize.ultraSmall)
            )

            Spacer(modifier = Modifier.height(Padding.small))

            LazyColumn {
                items(filteredAnimals) { animal ->
                    AnimalListItem(animal) {
                        onItemClick.invoke(it)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun AnimalListItem(animal: Animal, onItemClick: (animalId: String) -> Unit) {
    val (years, month) = DateUtil.getYearsAge(animal.birthDate)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Padding.small)
            .clickable { onItemClick.invoke(animal.id) },
        elevation = 0.dp,
        shape = Shape.medium,
        backgroundColor = HorseColor.surface,
        border = BorderStroke(CustomSize.borderThin, HorseColor.border)
    ) {
        Row(modifier = Modifier.padding(Padding.large)) {
            AsyncImage(
                model = animal.imageUrl,
                contentDescription = animal.name,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(CustomSize.animalCardImageSize)
                    .clip(Shape.small)
                    .background(HorseColor.surfaceVariant)
            )

            Spacer(modifier = Modifier.width(Padding.large))

            Column {
                Text(animal.name, color = HorseColor.textPrimary, style = MaterialTheme.typography.h6)
                Text(
                    "$years anos e $month meses",
                    color = HorseColor.textSecondary,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    "${DateUtil.getMonthsAge(animal.birthDate)} meses",
                    color = HorseColor.textSecondary,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Preview
@Composable
private fun AnimalListContentPreview() {
    HorseTheme {
        AnimalListContent(
            viewState = BaseViewState.Populated(listOf(Animal.mock, Animal.mock)),
            loadingDialogManager = LoadingDialogManager(),
            onSearchQueryChanged = {},
            onTypeFilterChanged = {},
            onItemClick = {},
            popBackStack = {},
            navigateToRegister = {}
        )
    }
}
