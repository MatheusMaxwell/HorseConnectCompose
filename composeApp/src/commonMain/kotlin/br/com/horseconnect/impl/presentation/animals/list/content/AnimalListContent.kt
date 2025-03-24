package br.com.horseconnect.impl.presentation.animals.list.content

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.AnimalType
import br.com.horseconnect.impl.presentation.animals.list.AnimalsViewState
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.loading.LoadingDialogManager
import br.com.horseconnect.theme.components.toolbar.Toolbar
import br.com.horseconnect.theme.dimens.padding.Padding
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import horseconnect.composeapp.generated.resources.Res
import horseconnect.composeapp.generated.resources.baseline_arrow_back_24
import horseconnect.composeapp.generated.resources.baseline_delete_24
import org.jetbrains.compose.resources.painterResource

@Composable
fun AnimalListContent(
    viewState: AnimalsViewState,
    loadingDialogManager: LoadingDialogManager,
    onSearchQueryChanged: (String) -> Unit,
    onTypeFilterChanged: (AnimalType?) -> Unit,
    onItemClick: (animalId: String) -> Unit,
    popBackStack: () -> Unit,
    navigateToRegister: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<AnimalType?>(null) }
    var animals: List<Animal> by remember{ mutableStateOf(emptyList()) }
    val toaster = LocalToasterDialog.current

    LaunchedEffect(viewState) {
        when(viewState) {
            AnimalsViewState.Initial -> Unit
            AnimalsViewState.Loading -> {
                loadingDialogManager.show()
            }
            is AnimalsViewState.Populated -> {
                loadingDialogManager.hide()
                animals = viewState.data
            }
            is AnimalsViewState.Error -> {
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
                backgroundColor = HorseColor.primary,
                contentColor = Color.White
            ) {
                Text("+", style = MaterialTheme.typography.h6)
            }
        },
        backgroundColor = HorseColor.background
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Barra de Busca
            SearchBar(query = searchQuery, onQueryChanged = {
                searchQuery = it
                onSearchQueryChanged(it)
            })

            Spacer(modifier = Modifier.height(16.dp))

            // Filtro de Tipo de Animal (Dropdown)
            AnimalTypeFilter(selectedType = selectedType, onTypeSelected = { selected ->
                selected.let {
                    selectedType = it
                    onTypeFilterChanged(it)
                }
            })

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Animais
            val filteredAnimals = animals.filter { animal ->
                (searchQuery.isEmpty() || animal.name.contains(searchQuery, ignoreCase = true)) &&
                        (selectedType == null || animal.types.contains(selectedType))
            }

            Text("${filteredAnimals.size} animais")

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

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 6.dp,
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color.White
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChanged,
            label = { Text("Buscar") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Transparent,
                textColor = Color.Black,
                focusedLabelColor = Color.Black
            )
        )
    }
}

@Composable
fun AnimalTypeFilter(
    selectedType: AnimalType?,
    onTypeSelected: (AnimalType?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val types = AnimalType.entries

    Column {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row {
                OutlinedButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(selectedType?.value ?: "Filtrar", style = TextStyle(color = Color.Black))
                }
                if (selectedType?.value != null) {
                    OutlinedButton(
                        modifier = Modifier.padding(start = Padding.small),
                        onClick = { onTypeSelected.invoke(null) },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(Res.drawable.baseline_delete_24),
                            contentDescription = "",
                            tint = Color.Black
                        )
                    }
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                types.forEach { type ->
                    DropdownMenuItem(onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }) {
                        Text(type.value, style = TextStyle(color = Color.Black))
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
            .padding(vertical = 8.dp)
            .clickable { onItemClick.invoke(animal.id) },
        elevation = 6.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Imagem do Animal
            AsyncImage(
                model = animal.imageUrl,
                contentDescription = animal.name,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(10.dp)).background(MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(animal.name, style = MaterialTheme.typography.h6)
                Text("$years anos e $month meses", style = MaterialTheme.typography.body2)
                Text("${DateUtil.getMonthsAge(animal.birthDate)} meses", style = MaterialTheme.typography.body2)
            }
        }
    }
}
