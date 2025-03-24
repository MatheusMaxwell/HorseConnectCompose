package br.com.horseconnect.impl.presentation.animals.register.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.horseconnect.common.manager.SessionManager
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.common.util.DateVisualTransformation
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.AnimalType
import br.com.horseconnect.impl.presentation.animals.register.AnimalRegisterViewState
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.button.PrimaryButton
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import br.com.horseconnect.theme.components.toolbar.Toolbar
import br.com.horseconnect.theme.dimens.padding.Padding
import coil3.compose.AsyncImage
import com.preat.peekaboo.image.picker.ResizeOptions
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.image.picker.toImageBitmap
import kotlin.math.pow
import kotlin.math.round
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterialApi::class, ExperimentalUuidApi::class)
@Composable
fun AnimalRegisterContent(
    viewState: AnimalRegisterViewState,
    onRegister: (Animal, ByteArray?) -> Unit,
    popBackStack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var coat by remember { mutableStateOf("") }
    var race by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var selectedTypes by remember { mutableStateOf<List<AnimalType>>(emptyList()) }
    var ownerOfSliderValue by remember { mutableStateOf(100f) }
    var ownerOfTextFieldValue by remember { mutableStateOf("100") }
    var image: ByteArray? by remember { mutableStateOf(null) }

    val enableRegisterButton by derivedStateOf {
        name.isNotEmpty() && birthDate.isNotEmpty() && coat.isNotEmpty() &&
                race.isNotEmpty() && sex.isNotEmpty() && selectedTypes.isNotEmpty()
    }

    val loading = LocalLoadingDialog.current
    val toaster = LocalToasterDialog.current

    LaunchedEffect(viewState) {
        when(viewState) {
            is AnimalRegisterViewState.Initial -> Unit
            is AnimalRegisterViewState.Loading -> {
                loading.show()
            }
            is AnimalRegisterViewState.RegisterSuccess -> {
                loading.hide()
                toaster.show("Animal cadastrado.")
                popBackStack.invoke()
            }
            is AnimalRegisterViewState.Error -> {
                loading.hide()
                toaster.show(viewState.msg)
            }
        }
    }

    Scaffold(
        backgroundColor = HorseColor.background,
        topBar = {
            Toolbar(
                title = "Cadastro",
                onNavigationAction = {
                    popBackStack.invoke()
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier.padding(Padding.medium).background(Color.White)
            ){
                PrimaryButton(
                    text = "Registrar",
                    enabled = enableRegisterButton,
                    onClick = {
                        val animal = Animal(
                            id = "", //Generated by firebase
                            birthDate = DateUtil.formatDateString(birthDate),
                            coat = coat,
                            farmId = SessionManager.farm?.id.orEmpty(),
                            imageId = "",
                            imageUrl = "",
                            isLive = true,
                            name = name,
                            race = race,
                            sex = sex,
                            types = selectedTypes,
                            ownerOf = ownerOfTextFieldValue.toDouble(),
                            otherOwners = emptyList()
                        )
                        onRegister(animal, image)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(Padding.medium)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(image = image) {
                image = it
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = birthDate,
                onValueChange = { newValue ->
                    birthDate = newValue
                },
                label = { Text("Data de Nascimento (dd/MM/yyyy)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = DateVisualTransformation()
            )

            OutlinedTextField(
                value = coat,
                onValueChange = { coat = it },
                label = { Text("Pelagem") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = race,
                onValueChange = { race = it },
                label = { Text("Raça") },
                modifier = Modifier.fillMaxWidth()
            )

            // Dropdown for Sex
            var expandedSex by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedSex,
                onExpandedChange = { expandedSex = !expandedSex }
            ) {
                OutlinedTextField(
                    value = sex,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sexo") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSex) }
                )
                ExposedDropdownMenu(
                    expanded = expandedSex,
                    onDismissRequest = { expandedSex = false }
                ) {
                    listOf("Macho", "Fêmea").forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                sex = option
                                expandedSex = false
                            }
                        ) {
                            Text(option)
                        }
                    }
                }
            }

            // Dropdown for AnimalType
            var expandedType by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedType,
                onExpandedChange = { expandedType = !expandedType }
            ) {
                OutlinedTextField(
                    value = if (selectedTypes.isEmpty()) "" else selectedTypes.joinToString { it.value },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Animal") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType) }
                )
                ExposedDropdownMenu(
                    expanded = expandedType,
                    onDismissRequest = { expandedType = false }
                ) {
                    AnimalType.entries.forEach { type ->
                        DropdownMenuItem(
                            onClick = {
                                selectedTypes = if (type in selectedTypes) {
                                    selectedTypes - type // Remove se já estiver selecionado
                                } else {
                                    selectedTypes + type // Adiciona se ainda não estiver selecionado
                                }
                            }
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = type in selectedTypes,
                                    onCheckedChange = null // Gerenciado pelo próprio DropdownMenuItem
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(type.value)
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(top = Padding.medium),
            ) {
                Text("Percentual de posse")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Slider
                    Slider(
                        value = ownerOfSliderValue,
                        onValueChange = { newValue ->
                            ownerOfSliderValue = newValue
                            ownerOfTextFieldValue = newValue.toInt().let {
                                if (it%5 == 0) {
                                    it
                                } else {
                                    it - (it % 5)
                                }
                            }.toString()
                        },
                        valueRange = 0f..100f,
                        steps = 20,
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = HorseColor.primary,
                            activeTrackColor = HorseColor.primary
                        )
                    )

                    // TextField
                    OutlinedTextField(
                        value = ownerOfTextFieldValue,
                        onValueChange = { newText ->
                            val sanitizedText = newText.filter { it.isDigit() || it == '.' } // Filtra para permitir apenas números e ponto
                            if (sanitizedText.isValidNumber() || sanitizedText.isEmpty()) {
                                ownerOfTextFieldValue = sanitizedText // Atualiza o TextField
                                ownerOfSliderValue = sanitizedText.toFloatOrNull() ?: ownerOfSliderValue // Atualiza o valor do slider
                            }
                        },
                        label = { Text("Valor:") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Decimal
                        ),
                        modifier = Modifier.width(100.dp) // Define a largura do TextField
                    )
                    Spacer(modifier = Modifier.width(Padding.small))
                    Text("%")
                }
            }
        }
    }
}

@Composable
private fun Image(image: ByteArray?, updateImage: (ByteArray) -> Unit){
    val scope = rememberCoroutineScope()
    val resizeOptions = ResizeOptions(
        resizeThresholdBytes = 2 * 1024 * 1024L, // Custom threshold for 2MB,
        compressionQuality = 0.5 // Adjust compression quality (0.0 to 1.0)
    )
    val singleImagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        resizeOptions = resizeOptions,
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let {
                updateImage.invoke(it)
            }
        }
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth().clickable {
            singleImagePicker.launch()
        }
    ) {
        if (image != null) {
            Image(
                bitmap = image.toImageBitmap(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color.LightGray, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Text("Clique para selecionar uma imagem")
            }
        }
    }
}

fun String.isValidNumber(): Boolean {
    return try {
        val value = this.toFloatOrNull()
        value != null && value in 0f..100f // Verifica se o valor está no intervalo de 0 a 100
    } catch (e: Exception) {
        false
    }
}