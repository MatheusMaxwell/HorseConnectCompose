package br.com.horseconnect.impl.presentation.finance.register.content

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
import br.com.horseconnect.impl.domain.model.FinanceEntry
import br.com.horseconnect.impl.domain.model.FinanceIntervalUnit
import br.com.horseconnect.impl.domain.model.FinanceType
import br.com.horseconnect.impl.domain.model.Installment
import br.com.horseconnect.impl.presentation.finance.FinanceOptions
import br.com.horseconnect.impl.presentation.finance.register.FinanceRegisterViewState
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.button.PrimaryButton
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import br.com.horseconnect.theme.components.toolbar.Toolbar
import br.com.horseconnect.theme.dimens.padding.Padding
import org.jetbrains.compose.ui.tooling.preview.Preview

private enum class LaunchMode(val label: String) {
    SINGLE("Único"),
    RECURRING("Recorrente"),
    INSTALLMENT("Parcelado")
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FinanceRegisterContent(
    viewState: FinanceRegisterViewState,
    currentEntry: FinanceEntry? = null,
    onRegister: (FinanceEntry) -> Unit,
    onUpdate: (FinanceEntry) -> Unit,
    popBackStack: () -> Unit
) {
    val isEditMode = currentEntry != null

    var type by remember { mutableStateOf(currentEntry?.type ?: FinanceType.EXPENSE) }
    var description by remember { mutableStateOf(currentEntry?.description.orEmpty()) }
    var category by remember { mutableStateOf(currentEntry?.category.orEmpty()) }
    var customCategory by remember { mutableStateOf("") }
    var amountText by remember {
        mutableStateOf(currentEntry?.amount?.let { formatAmountForInput(it) }.orEmpty())
    }
    var date by remember {
        mutableStateOf(DateUtil.getDateFormatter(currentEntry?.date.orEmpty()).replace("/", ""))
    }
    var mode by remember {
        mutableStateOf(
            when {
                currentEntry?.isRecurring == true -> LaunchMode.RECURRING
                currentEntry?.isInstallment == true -> LaunchMode.INSTALLMENT
                else -> LaunchMode.SINGLE
            }
        )
    }
    var intervalValue by remember { mutableStateOf(currentEntry?.recurrenceIntervalValue?.takeIf { it > 0 }?.toString().orEmpty()) }
    var intervalUnit by remember { mutableStateOf(currentEntry?.recurrenceIntervalUnit ?: FinanceIntervalUnit.MONTHS) }
    var recurrenceEndDate by remember {
        mutableStateOf(DateUtil.getDateFormatter(currentEntry?.recurrenceEndDate.orEmpty()).replace("/", ""))
    }
    var installmentCount by remember {
        mutableStateOf(currentEntry?.installments?.size?.takeIf { it > 0 }?.toString().orEmpty())
    }
    var notes by remember { mutableStateOf(currentEntry?.notes.orEmpty()) }

    val categoryOptions = if (type == FinanceType.INCOME) FinanceOptions.incomeCategories else FinanceOptions.expenseCategories
    val resolvedCategory = if (category == "Outra") customCategory else category

    val enableFinishButton by derivedStateOf {
        val baseValid = description.isNotEmpty() &&
                resolvedCategory.isNotEmpty() &&
                amountText.toAmountOrNull() != null &&
                date.length == 8
        val modeValid = when (mode) {
            LaunchMode.SINGLE -> true
            LaunchMode.RECURRING -> intervalValue.isNotEmpty()
            LaunchMode.INSTALLMENT -> (installmentCount.toIntOrNull() ?: 0) >= 2
        }
        baseValid && modeValid
    }

    val loading = LocalLoadingDialog.current
    val toaster = LocalToasterDialog.current

    LaunchedEffect(viewState) {
        when(viewState) {
            is FinanceRegisterViewState.Initial -> Unit
            is FinanceRegisterViewState.Loading -> {
                loading.show()
            }
            is FinanceRegisterViewState.RegisterSuccess -> {
                loading.hide()
                toaster.show(if (isEditMode) "Lançamento atualizado." else "Lançamento registrado.")
                popBackStack.invoke()
            }
            is FinanceRegisterViewState.Error -> {
                loading.hide()
                toaster.show(viewState.msg)
            }
        }
    }

    Scaffold(
        backgroundColor = HorseColor.background,
        topBar = {
            Toolbar(
                title = if (isEditMode) "Editar Lançamento" else "Novo Lançamento",
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
                        val amount = amountText.toAmountOrNull() ?: 0.0

                        val recurrenceIntervalValueParsed = intervalValue.toIntOrNull() ?: 0
                        val isoEndDate = if (recurrenceEndDate.length == 8) {
                            DateUtil.convertToISO8601(DateUtil.formatDateString(recurrenceEndDate))
                        } else {
                            ""
                        }

                        val installments = if (mode == LaunchMode.INSTALLMENT) {
                            buildInstallments(isoDate, amount, installmentCount.toIntOrNull() ?: 0)
                        } else {
                            emptyList()
                        }

                        val entry = FinanceEntry(
                            id = currentEntry?.id.orEmpty(),
                            farmId = SessionManager.farm?.id.orEmpty(),
                            type = type,
                            description = description,
                            category = resolvedCategory,
                            amount = amount,
                            date = isoDate,
                            isRecurring = mode == LaunchMode.RECURRING,
                            recurrenceIntervalValue = if (mode == LaunchMode.RECURRING) recurrenceIntervalValueParsed else 0,
                            recurrenceIntervalUnit = intervalUnit,
                            recurrenceEndDate = if (mode == LaunchMode.RECURRING) isoEndDate else "",
                            isInstallment = mode == LaunchMode.INSTALLMENT,
                            installments = installments,
                            notes = notes
                        )

                        if (isEditMode) {
                            onUpdate(entry)
                        } else {
                            onRegister(entry)
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
                label = "Tipo",
                options = FinanceType.entries.map { it.label },
                selected = type.label,
                onSelected = { selected ->
                    type = FinanceType.entries.first { it.label == selected }
                    category = ""
                    customCategory = ""
                }
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )

            OptionDropdownField(
                label = "Categoria",
                options = categoryOptions,
                selected = category,
                onSelected = { category = it }
            )

            if (category == "Outra") {
                OutlinedTextField(
                    value = customCategory,
                    onValueChange = { customCategory = it },
                    label = { Text("Nome da categoria") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            OutlinedTextField(
                value = amountText,
                onValueChange = { newValue -> amountText = newValue.filter { it.isDigit() || it == ',' } },
                label = { Text("Valor (R$)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = date,
                onValueChange = { newValue -> date = newValue.filter { it.isDigit() }.take(8) },
                label = { Text("Data (dd/MM/yyyy)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = DateVisualTransformation()
            )

            OptionDropdownField(
                label = "Lançamento",
                options = LaunchMode.entries.map { it.label },
                selected = mode.label,
                onSelected = { selected ->
                    mode = LaunchMode.entries.first { it.label == selected }
                }
            )

            if (mode == LaunchMode.RECURRING) {
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
                            options = FinanceIntervalUnit.entries.map { it.label },
                            selected = intervalUnit.label,
                            onSelected = { selected ->
                                intervalUnit = FinanceIntervalUnit.entries.first { it.label == selected }
                            }
                        )
                    }
                }

                OutlinedTextField(
                    value = recurrenceEndDate,
                    onValueChange = { newValue -> recurrenceEndDate = newValue.filter { it.isDigit() }.take(8) },
                    label = { Text("Data fim (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = DateVisualTransformation()
                )
            }

            if (mode == LaunchMode.INSTALLMENT) {
                OutlinedTextField(
                    value = installmentCount,
                    onValueChange = { newValue -> installmentCount = newValue.filter { it.isDigit() } },
                    label = { Text("Número de parcelas") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Observações (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun String.toAmountOrNull(): Double? {
    return replace(',', '.').toDoubleOrNull()?.takeIf { it > 0 }
}

private fun formatAmountForInput(amount: Double): String {
    return amount.toString().replace('.', ',')
}

private fun buildInstallments(startDate: String, totalAmount: Double, count: Int): List<Installment> {
    if (count <= 0) return emptyList()

    val baseAmountCents = (totalAmount * 100).toLong() / count
    val remainderCents = (totalAmount * 100).toLong() - (baseAmountCents * count)

    return (0 until count).map { index ->
        val installmentCents = baseAmountCents + if (index == count - 1) remainderCents else 0
        Installment(
            number = index + 1,
            amount = installmentCents / 100.0,
            dueDate = DateUtil.addMonths(startDate, index)
        )
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
private fun FinanceRegisterContentPreview() {
    HorseTheme {
        FinanceRegisterContent(
            viewState = FinanceRegisterViewState.Initial,
            onRegister = {},
            onUpdate = {},
            popBackStack = {}
        )
    }
}
