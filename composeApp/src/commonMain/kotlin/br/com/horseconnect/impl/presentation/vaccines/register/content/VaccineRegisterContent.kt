package br.com.horseconnect.impl.presentation.vaccines.register.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.horseconnect.common.manager.SessionManager
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.common.util.DateVisualTransformation
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.Vaccine
import br.com.horseconnect.impl.domain.model.VaccineIntervalUnit
import br.com.horseconnect.impl.presentation.vaccines.VaccineOptions
import br.com.horseconnect.impl.presentation.vaccines.register.VaccineRegisterViewState
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.button.PrimaryButton
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import br.com.horseconnect.theme.components.toolbar.Toolbar
import br.com.horseconnect.theme.dimens.padding.Padding
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VaccineRegisterContent(
    viewState: VaccineRegisterViewState,
    currentGroup: List<Vaccine>? = null,
    animals: List<Animal>,
    loadAnimals: () -> Unit,
    onSubmit: (appliedVaccines: List<Vaccine>) -> Unit,
    onUpdateGroup: (toCreate: List<Vaccine>, toUpdate: List<Vaccine>, toDeleteIds: List<String>) -> Unit = { _, _, _ -> },
    popBackStack: () -> Unit
) {
    val isEditMode = !currentGroup.isNullOrEmpty()
    val referenceVaccine = currentGroup?.firstOrNull()

    var name by remember { mutableStateOf(referenceVaccine?.name.orEmpty()) }
    var customName by remember { mutableStateOf("") }
    var date by remember {
        mutableStateOf(DateUtil.getDateFormatter(referenceVaccine?.applicationDate.orEmpty()).replace("/", ""))
    }
    var intervalValue by remember { mutableStateOf(referenceVaccine?.repeatIntervalValue?.toString().orEmpty()) }
    var intervalUnit by remember { mutableStateOf(referenceVaccine?.repeatIntervalUnit ?: VaccineIntervalUnit.MONTHS) }
    var veterinarian by remember { mutableStateOf(referenceVaccine?.veterinarian.orEmpty()) }
    var batch by remember { mutableStateOf(referenceVaccine?.batch.orEmpty()) }
    var notes by remember { mutableStateOf(referenceVaccine?.notes.orEmpty()) }
    var selectedAnimals by remember {
        mutableStateOf<List<Animal>>(
            currentGroup?.map { vaccine -> Animal.mock.copy(id = vaccine.animalId, name = vaccine.animalName) }
                ?: emptyList()
        )
    }

    val resolvedName = if (name == "Outra") customName else name

    val enableFinishButton by derivedStateOf {
        resolvedName.isNotEmpty() && date.length == 8 && intervalValue.isNotEmpty() && selectedAnimals.isNotEmpty()
    }

    val loading = LocalLoadingDialog.current
    val toaster = LocalToasterDialog.current

    LaunchedEffect(Unit) {
        loadAnimals.invoke()
    }

    LaunchedEffect(viewState) {
        when(viewState) {
            is VaccineRegisterViewState.Initial -> Unit
            is VaccineRegisterViewState.Loading -> {
                loading.show()
            }
            is VaccineRegisterViewState.RegisterSuccess -> {
                loading.hide()
                toaster.show(if (isEditMode) "Vacina atualizada." else "Vacinação registrada.")
                popBackStack.invoke()
            }
            is VaccineRegisterViewState.Error -> {
                loading.hide()
                toaster.show(viewState.msg)
            }
        }
    }

    Scaffold(
        backgroundColor = HorseColor.background,
        topBar = {
            Toolbar(
                title = if (isEditMode) "Editar Vacina" else "Cadastro de Vacina",
                onNavigationAction = {
                    popBackStack.invoke()
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier.imePadding().padding(Padding.medium).background(HorseColor.background)
            ) {
                PrimaryButton(
                    text = "Finalizar",
                    enabled = enableFinishButton,
                    onClick = {
                        val isoDate = DateUtil.convertToISO8601(DateUtil.formatDateString(date))
                        val intervalIntValue = intervalValue.toIntOrNull() ?: 0
                        val nextDueDate = if (intervalUnit == VaccineIntervalUnit.MONTHS) {
                            DateUtil.addMonths(isoDate, intervalIntValue)
                        } else {
                            DateUtil.addDays(isoDate, intervalIntValue)
                        }

                        if (isEditMode) {
                            val existingByAnimalId = currentGroup.orEmpty().associateBy { it.animalId }
                            val selectedIds = selectedAnimals.map { it.id }.toSet()

                            val toUpdate = selectedAnimals.mapNotNull { animal ->
                                existingByAnimalId[animal.id]?.copy(
                                    name = resolvedName,
                                    repeatIntervalValue = intervalIntValue,
                                    repeatIntervalUnit = intervalUnit,
                                    applicationDate = isoDate,
                                    nextDueDate = nextDueDate,
                                    veterinarian = veterinarian,
                                    batch = batch,
                                    notes = notes,
                                    animalName = animal.name
                                )
                            }

                            val toCreate = selectedAnimals.filter { !existingByAnimalId.containsKey(it.id) }.map { animal ->
                                Vaccine(
                                    id = "",
                                    farmId = SessionManager.farm?.id.orEmpty(),
                                    name = resolvedName,
                                    repeatIntervalValue = intervalIntValue,
                                    repeatIntervalUnit = intervalUnit,
                                    applicationDate = isoDate,
                                    nextDueDate = nextDueDate,
                                    veterinarian = veterinarian,
                                    batch = batch,
                                    notes = notes,
                                    animalId = animal.id,
                                    animalName = animal.name
                                )
                            }

                            val toDeleteIds = currentGroup.orEmpty()
                                .filter { it.animalId !in selectedIds }
                                .map { it.id }

                            onUpdateGroup(toCreate, toUpdate, toDeleteIds)
                        } else {
                            val appliedVaccines = selectedAnimals.map { animal ->
                                Vaccine(
                                    id = "",
                                    farmId = SessionManager.farm?.id.orEmpty(),
                                    name = resolvedName,
                                    repeatIntervalValue = intervalIntValue,
                                    repeatIntervalUnit = intervalUnit,
                                    applicationDate = isoDate,
                                    nextDueDate = nextDueDate,
                                    veterinarian = veterinarian,
                                    batch = batch,
                                    notes = notes,
                                    animalId = animal.id,
                                    animalName = animal.name
                                )
                            }
                            onSubmit(appliedVaccines)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(bottom = 80.dp)
                .verticalScroll(rememberScrollState())
                .padding(Padding.medium),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OptionDropdownField(
                label = "Vacina",
                options = VaccineOptions.names,
                selected = name,
                onSelected = { name = it }
            )

            if (name == "Outra") {
                OutlinedTextField(
                    value = customName,
                    onValueChange = { customName = it },
                    label = { Text("Nome da vacina") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            OutlinedTextField(
                value = date,
                onValueChange = { newValue -> date = newValue.filter { it.isDigit() }.take(8) },
                label = { Text("Data de aplicação (dd/MM/yyyy)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = DateVisualTransformation()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = intervalValue,
                    onValueChange = { newValue -> intervalValue = newValue.filter { it.isDigit() } },
                    label = { Text("Repete a cada") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(Padding.small))
                Box(modifier = Modifier.weight(1f)) {
                    OptionDropdownField(
                        label = "Unidade",
                        options = VaccineIntervalUnit.entries.map { it.label },
                        selected = intervalUnit.label,
                        onSelected = { selected ->
                            intervalUnit = VaccineIntervalUnit.entries.first { it.label == selected }
                        }
                    )
                }
            }

            OutlinedTextField(
                value = veterinarian,
                onValueChange = { veterinarian = it },
                label = { Text("Veterinário (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = batch,
                onValueChange = { batch = it },
                label = { Text("Lote (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Observações (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            AnimalMultiSelectField(
                animals = animals,
                selectedAnimals = selectedAnimals,
                onSelectionChanged = { selectedAnimals = it }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun OptionDropdownField(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                ) {
                    Text(option)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AnimalMultiSelectField(
    animals: List<Animal>,
    selectedAnimals: List<Animal>,
    onSelectionChanged: (List<Animal>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = if (selectedAnimals.isEmpty()) "" else selectedAnimals.joinToString { it.name },
            onValueChange = {},
            readOnly = true,
            label = { Text("Animais") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            animals.forEach { animal ->
                DropdownMenuItem(
                    onClick = {
                        onSelectionChanged(
                            if (animal in selectedAnimals) {
                                selectedAnimals - animal
                            } else {
                                selectedAnimals + animal
                            }
                        )
                    }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = animal in selectedAnimals,
                            onCheckedChange = null
                        )
                        Spacer(modifier = Modifier.width(Padding.small))
                        Text(animal.name)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun VaccineRegisterContentPreview() {
    HorseTheme {
        VaccineRegisterContent(
            viewState = VaccineRegisterViewState.Initial,
            animals = listOf(Animal.mock),
            loadAnimals = {},
            onSubmit = {},
            popBackStack = {}
        )
    }
}
