package br.com.horseconnect.impl.presentation.finance.detail.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import br.com.horseconnect.common.ext.toCurrencyString
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.impl.domain.model.FinanceEntry
import br.com.horseconnect.impl.domain.model.FinanceType
import br.com.horseconnect.impl.presentation.finance.detail.FinanceDetailViewState
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.button.PrimaryButton
import br.com.horseconnect.theme.components.dialog.ConfirmAction
import br.com.horseconnect.theme.components.dialog.CustomDialog
import br.com.horseconnect.theme.components.dialog.DismissAction
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import br.com.horseconnect.theme.components.titlevalue.TitleValue
import br.com.horseconnect.theme.components.toolbar.Toolbar
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.shape.Shape
import br.com.horseconnect.theme.dimens.size.CustomSize
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FinanceDetailContent(
    viewState: FinanceDetailViewState,
    entry: FinanceEntry,
    popBackStack: () -> Unit,
    deleteEntry: (FinanceEntry) -> Unit,
    navigateToEdit: (entry: FinanceEntry) -> Unit
) {
    val toaster = LocalToasterDialog.current
    val loading = LocalLoadingDialog.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewState) {
        when(viewState) {
            FinanceDetailViewState.Initial -> Unit
            FinanceDetailViewState.Loading -> {
                loading.show()
            }
            is FinanceDetailViewState.Error -> {
                loading.hide()
                toaster.show(viewState.msg)
            }
            FinanceDetailViewState.DeletedSuccess -> {
                loading.hide()
                toaster.show("Lançamento excluído com sucesso.")
                popBackStack.invoke()
            }
        }
    }

    DeleteEntryDialog(
        showDeleteDialog = showDeleteDialog,
        onDismiss = { showDeleteDialog = false },
        onConfirmDelete = {
            deleteEntry.invoke(entry)
        }
    )

    Scaffold(
        topBar = {
            Toolbar(
                title = entry.description,
                onNavigationAction = {
                    popBackStack.invoke()
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier.padding(Padding.medium).background(HorseColor.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PrimaryButton(
                    text = "Editar",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navigateToEdit.invoke(entry) }
                )
                Text(
                    text = "Excluir lançamento",
                    modifier = Modifier
                        .clickable { showDeleteDialog = true }
                        .padding(Padding.medium),
                    style = TextStyle(
                        color = HorseColor.error,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        },
        backgroundColor = HorseColor.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(innerPadding)
                .padding(Padding.medium)
        ) {
            val isIncome = entry.type == FinanceType.INCOME
            Text(
                text = "${if (isIncome) "+ " else "- "}${entry.amount.toCurrencyString()}",
                color = if (isIncome) HorseColor.success else HorseColor.error,
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(Padding.small))

            TitleValue("Tipo", value = entry.type.label)
            TitleValue("Categoria", value = entry.category)
            TitleValue("Data", value = DateUtil.getDateFormatter(entry.date))

            if (entry.isRecurring) {
                TitleValue(
                    "Repete a cada",
                    value = "${entry.recurrenceIntervalValue} ${entry.recurrenceIntervalUnit.label}"
                )
                if (entry.recurrenceEndDate.isNotEmpty()) {
                    TitleValue("Termina em", value = DateUtil.getDateFormatter(entry.recurrenceEndDate))
                }
            }

            if (entry.notes.isNotEmpty()) {
                TitleValue("Observações", value = entry.notes)
            }

            if (entry.isInstallment && entry.installments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Padding.medium))
                Text(
                    "Parcelas",
                    color = HorseColor.textSecondary,
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = Modifier.height(Padding.small))
                entry.installments.forEach { installment ->
                    InstallmentItem(number = installment.number, amount = installment.amount, dueDate = installment.dueDate)
                }
            }
        }
    }
}

@Composable
private fun InstallmentItem(number: Int, amount: Double, dueDate: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = Padding.small),
        elevation = 0.dp,
        shape = Shape.medium,
        backgroundColor = HorseColor.surface,
        border = BorderStroke(CustomSize.borderThin, HorseColor.border)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(Padding.large),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Parcela $number", color = HorseColor.textPrimary, style = MaterialTheme.typography.body2)
            Text(DateUtil.getDateFormatter(dueDate), color = HorseColor.textSecondary, style = MaterialTheme.typography.body2)
            Text(amount.toCurrencyString(), color = HorseColor.textPrimary, style = MaterialTheme.typography.body2)
        }
    }
}

@Composable
private fun DeleteEntryDialog(
    showDeleteDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    CustomDialog(
        visible = showDeleteDialog,
        title = "Excluir lançamento",
        message = "Tem certeza que deseja excluir este lançamento?",
        onDismiss = onDismiss,
        confirmAction = ConfirmAction(
            text = "Sim",
            onClick = onConfirmDelete
        ),
        dismissAction = DismissAction(
            text = "Não",
            onClick = onDismiss
        )
    )
}

@Preview
@Composable
private fun FinanceDetailContentPreview() {
    HorseTheme {
        FinanceDetailContent(
            viewState = FinanceDetailViewState.Initial,
            entry = FinanceEntry.mock,
            popBackStack = {},
            deleteEntry = {},
            navigateToEdit = {}
        )
    }
}
