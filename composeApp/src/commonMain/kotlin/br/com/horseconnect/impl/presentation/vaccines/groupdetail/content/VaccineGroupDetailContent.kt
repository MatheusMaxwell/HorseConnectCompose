package br.com.horseconnect.impl.presentation.vaccines.groupdetail.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.clickable
import androidx.compose.material.Card
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.impl.domain.model.Vaccine
import br.com.horseconnect.impl.presentation.vaccines.groupdetail.VaccineGroupDetailViewState
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
import horseconnect.composeapp.generated.resources.Res
import horseconnect.composeapp.generated.resources.baseline_health_24
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun VaccineGroupDetailContent(
    viewState: VaccineGroupDetailViewState,
    vaccines: List<Vaccine>,
    popBackStack: () -> Unit,
    deleteGroup: (ids: List<String>) -> Unit,
    navigateToEdit: (vaccines: List<Vaccine>) -> Unit
) {
    val vaccine = vaccines.firstOrNull() ?: Vaccine.mock
    val toaster = LocalToasterDialog.current
    val loading = LocalLoadingDialog.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewState) {
        when(viewState) {
            VaccineGroupDetailViewState.Initial -> Unit
            VaccineGroupDetailViewState.Loading -> {
                loading.show()
            }
            is VaccineGroupDetailViewState.Error -> {
                loading.hide()
                toaster.show(viewState.msg)
            }
            VaccineGroupDetailViewState.DeletedSuccess -> {
                loading.hide()
                toaster.show("Vacina excluída com sucesso.")
                popBackStack.invoke()
            }
        }
    }

    DeleteGroupDialog(
        showDeleteDialog = showDeleteDialog,
        onDismiss = { showDeleteDialog = false },
        onConfirmDelete = {
            deleteGroup.invoke(vaccines.map { it.id })
        }
    )

    Scaffold(
        topBar = {
            Toolbar(
                title = vaccine.name,
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
                    onClick = { navigateToEdit.invoke(vaccines) }
                )
                Text(
                    text = "Excluir vacina",
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
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(Padding.medium)
        ) {
            TitleValue("Aplicada em", value = DateUtil.getDateFormatter(vaccine.applicationDate))
            TitleValue("Próxima dose", value = DateUtil.getDateFormatter(vaccine.nextDueDate))
            TitleValue(
                "Repete a cada",
                value = "${vaccine.repeatIntervalValue} ${vaccine.repeatIntervalUnit.label}"
            )
            if (vaccine.veterinarian.isNotEmpty()) {
                TitleValue("Veterinário", value = vaccine.veterinarian)
            }
            if (vaccine.batch.isNotEmpty()) {
                TitleValue("Lote", value = vaccine.batch)
            }
            if (vaccine.notes.isNotEmpty()) {
                TitleValue("Observações", value = vaccine.notes)
            }

            Spacer(modifier = Modifier.height(Padding.medium))

            Text(
                text = if (vaccines.size > 1) "${vaccines.size} animais vacinados" else "1 animal vacinado",
                color = HorseColor.textSecondary,
                style = MaterialTheme.typography.subtitle1
            )

            Spacer(modifier = Modifier.height(Padding.small))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(vaccines) { animalVaccine ->
                    AnimalListItem(animalVaccine.animalName)
                }
            }
        }
    }
}

@Composable
private fun AnimalListItem(animalName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Padding.small),
        elevation = 0.dp,
        shape = Shape.medium,
        backgroundColor = HorseColor.surface,
        border = BorderStroke(CustomSize.borderThin, HorseColor.border)
    ) {
        Text(
            modifier = Modifier.padding(Padding.medium),
            text = animalName,
            color = HorseColor.textPrimary,
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
private fun DeleteGroupDialog(
    showDeleteDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    CustomDialog(
        visible = showDeleteDialog,
        title = "Excluir vacina",
        message = "Tem certeza que deseja excluir esta vacina e todos os registros de animais vacinados?",
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
private fun VaccineGroupDetailContentPreview() {
    HorseTheme {
        VaccineGroupDetailContent(
            viewState = VaccineGroupDetailViewState.Initial,
            vaccines = listOf(Vaccine.mock, Vaccine.mock),
            popBackStack = {},
            deleteGroup = {},
            navigateToEdit = {}
        )
    }
}
