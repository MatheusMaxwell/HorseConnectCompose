package br.com.horseconnect.impl.presentation.pregnancies.register.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.horseconnect.common.manager.SessionManager
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.common.util.DateVisualTransformation
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.AnimalType
import br.com.horseconnect.impl.domain.model.Pregnancy
import br.com.horseconnect.impl.presentation.pregnancies.PregnancyOptions
import br.com.horseconnect.impl.presentation.pregnancies.register.PregnancyRegisterViewState
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.button.PrimaryButton
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import br.com.horseconnect.theme.components.toolbar.Toolbar
import br.com.horseconnect.theme.dimens.padding.Padding
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PregnancyRegisterContent(
    viewState: PregnancyRegisterViewState,
    currentPregnancy: Pregnancy?,
    animals: List<Animal>,
    loadAnimals: () -> Unit,
    onRegister: (Pregnancy) -> Unit,
    onUpdate: (Pregnancy) -> Unit,
    popBackStack: () -> Unit
) {
    var date by remember { mutableStateOf(DateUtil.getDateFormatter(currentPregnancy?.date.orEmpty()).replace("/", "")) }
    var femaleId by remember { mutableStateOf(currentPregnancy?.femaleId.orEmpty()) }
    var femaleName by remember { mutableStateOf(currentPregnancy?.femaleName.orEmpty()) }
    var maleId by remember { mutableStateOf(currentPregnancy?.maleId.orEmpty()) }
    var maleName by remember { mutableStateOf(currentPregnancy?.maleName.orEmpty()) }
    var receiverId by remember { mutableStateOf(currentPregnancy?.receiverId.orEmpty()) }
    var expectedSex by remember { mutableStateOf(currentPregnancy?.sex.orEmpty()) }
    var status by remember { mutableStateOf(currentPregnancy?.status.orEmpty()) }
    var type by remember { mutableStateOf(currentPregnancy?.type.orEmpty()) }

    val enableRegisterButton by derivedStateOf {
        date.isNotEmpty() && femaleId.isNotEmpty() && maleId.isNotEmpty() &&
                expectedSex.isNotEmpty() && status.isNotEmpty() && type.isNotEmpty()
    }

    val loading = LocalLoadingDialog.current
    val toaster = LocalToasterDialog.current

    LaunchedEffect(Unit) {
        loadAnimals.invoke()
    }

    LaunchedEffect(viewState) {
        when(viewState) {
            is PregnancyRegisterViewState.Initial -> Unit
            is PregnancyRegisterViewState.Loading -> {
                loading.show()
            }
            is PregnancyRegisterViewState.RegisterSuccess -> {
                loading.hide()
                toaster.show("Cruzamento salvo.")
                popBackStack.invoke()
            }
            is PregnancyRegisterViewState.Error -> {
                loading.hide()
                toaster.show(viewState.msg)
            }
        }
    }

    Scaffold(
        backgroundColor = HorseColor.background,
        topBar = {
            Toolbar(
                title = "Cadastro de Cruzamento",
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
                    enabled = enableRegisterButton,
                    onClick = {
                        val isoDate = DateUtil.convertToISO8601(DateUtil.formatDateString(date))
                        if (currentPregnancy == null) {
                            val pregnancy = Pregnancy(
                                id = "", //Generated by firebase
                                date = isoDate,
                                farmId = SessionManager.farm?.id.orEmpty(),
                                femaleId = femaleId,
                                femaleName = femaleName,
                                maleId = maleId,
                                maleName = maleName,
                                receiverId = receiverId,
                                sex = expectedSex,
                                status = status,
                                type = type
                            )
                            onRegister(pregnancy)
                        } else {
                            val pregnancy = currentPregnancy.copy(
                                date = isoDate,
                                farmId = SessionManager.farm?.id.orEmpty(),
                                femaleId = femaleId,
                                femaleName = femaleName,
                                maleId = maleId,
                                maleName = maleName,
                                receiverId = receiverId,
                                sex = expectedSex,
                                status = status,
                                type = type
                            )
                            onUpdate(pregnancy)
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
            OutlinedTextField(
                value = date,
                onValueChange = { newValue -> date = newValue },
                label = { Text("Data do cruzamento (dd/MM/yyyy)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = DateVisualTransformation()
            )

            AnimalPickerField(
                label = "Fêmea",
                animals = animals.filter { it.sex == "Fêmea" },
                selectedName = femaleName,
                onSelected = {
                    femaleId = it.id
                    femaleName = it.name
                }
            )

            AnimalPickerField(
                label = "Macho",
                animals = animals.filter { it.sex == "Macho" },
                selectedName = maleName,
                onSelected = {
                    maleId = it.id
                    maleName = it.name
                }
            )

            AnimalPickerField(
                label = "Receptora (opcional)",
                animals = animals.filter { it.types.contains(AnimalType.RECEIVER) },
                selectedName = animals.firstOrNull { it.id == receiverId }?.name.orEmpty(),
                allowNone = true,
                onClear = { receiverId = "" },
                onSelected = {
                    receiverId = it.id
                }
            )

            OptionDropdownField(
                label = "Sexo previsto da cria",
                options = PregnancyOptions.expectedSexes,
                selected = expectedSex,
                onSelected = { expectedSex = it }
            )

            OptionDropdownField(
                label = "Status",
                options = PregnancyOptions.statuses,
                selected = status,
                onSelected = { status = it }
            )

            OptionDropdownField(
                label = "Tipo",
                options = PregnancyOptions.types,
                selected = type,
                onSelected = { type = it }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AnimalPickerField(
    label: String,
    animals: List<Animal>,
    selectedName: String,
    allowNone: Boolean = false,
    onClear: () -> Unit = {},
    onSelected: (Animal) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedName,
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
            if (allowNone) {
                DropdownMenuItem(
                    onClick = {
                        onClear.invoke()
                        expanded = false
                    }
                ) {
                    Text("Nenhuma")
                }
            }
            animals.forEach { animal ->
                DropdownMenuItem(
                    onClick = {
                        onSelected(animal)
                        expanded = false
                    }
                ) {
                    Text(animal.name)
                }
            }
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

@Preview
@Composable
private fun PregnancyRegisterContentPreview() {
    HorseTheme {
        PregnancyRegisterContent(
            viewState = PregnancyRegisterViewState.Initial,
            currentPregnancy = null,
            animals = listOf(Animal.mock),
            loadAnimals = {},
            onRegister = {},
            onUpdate = {},
            popBackStack = {}
        )
    }
}
