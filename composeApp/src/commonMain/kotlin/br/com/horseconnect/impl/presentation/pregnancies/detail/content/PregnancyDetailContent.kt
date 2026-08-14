package br.com.horseconnect.impl.presentation.pregnancies.detail.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.impl.domain.model.Pregnancy
import br.com.horseconnect.impl.presentation.pregnancies.detail.PregnancyDetailViewState
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
fun PregnancyDetailContent(
    viewState: PregnancyDetailViewState,
    pregnancy: Pregnancy,
    popBackStack: () -> Unit,
    deletePregnancy: (Pregnancy) -> Unit,
    navigateToEditPregnancy: (pregnancy: Pregnancy) -> Unit
) {
    val toaster = LocalToasterDialog.current
    val loading = LocalLoadingDialog.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewState) {
        when(viewState) {
            PregnancyDetailViewState.Initial -> Unit
            PregnancyDetailViewState.Loading -> {
                loading.show()
            }
            is PregnancyDetailViewState.Error -> {
                loading.hide()
                toaster.show(viewState.msg)
            }
            PregnancyDetailViewState.DeletedSuccess -> {
                loading.hide()
                toaster.show("Cruzamento excluído com sucesso.")
                popBackStack.invoke()
            }
        }
    }

    DeletePregnancyDialog(
        showDeleteDialog = showDeleteDialog,
        onDismiss = { showDeleteDialog = false },
        onConfirmDelete = {
            deletePregnancy.invoke(pregnancy)
        }
    )

    Scaffold(
        topBar = {
            Toolbar(
                onNavigationAction = {
                    popBackStack.invoke()
                }
            )
        },
        backgroundColor = HorseColor.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(Padding.medium)
        ) {
            PregnancyHeaderRow(
                pregnancy = pregnancy
            )
            TitleValue("Data do cruzamento", value = DateUtil.getDateFormatter(pregnancy.date))
            TitleValue("Previsão de nascimento", value = DateUtil.calculateBirthForecast(pregnancy.date))
            if (pregnancy.receiverId.isNotEmpty()) {
                TitleValue("Receptora", value = pregnancy.receiverId)
            }
            TitleValue("Sexo", value = pregnancy.sex)
            TitleValue("Status", value = pregnancy.status)
            TitleValue("Tipo", value = pregnancy.type)
            Spacer(modifier = Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth().padding(Padding.medium),
                    text = "Editar"
                ) {
                    navigateToEditPregnancy.invoke(pregnancy)
                }
                Text(
                    text = "Excluir cruzamento",
                    modifier = Modifier
                        .clickable { showDeleteDialog = true }
                        .padding(Padding.medium),
                    style = TextStyle(
                        color = HorseColor.error,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        }
    }
}

@Composable
private fun PregnancyHeaderRow(
    pregnancy: Pregnancy
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(bottom = Padding.medium)
    ) {
        AnimalCard(pregnancy.maleName)
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "x",
            color = HorseColor.textPrimary,
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )
        AnimalCard(pregnancy.femaleName)
    }
}

@Composable
private fun AnimalCard(name: String) {
    Card(
        modifier = Modifier.padding(Padding.medium),
        elevation = 0.dp,
        shape = Shape.small,
        backgroundColor = HorseColor.surface,
        border = BorderStroke(CustomSize.borderThin, HorseColor.border)
    ){
        Text(
            modifier = Modifier.fillMaxWidth().padding(vertical = Padding.medium),
            text = name,
            color = HorseColor.textPrimary,
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun DeletePregnancyDialog(
    showDeleteDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    CustomDialog(
        visible = showDeleteDialog,
        title = "Excluir cruzamento",
        message = "Tem certeza que deseja excluir este cruzamento?",
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
private fun PregnancyDetailContentPreview() {
    HorseTheme {
        PregnancyDetailContent(
            viewState = PregnancyDetailViewState.Initial,
            pregnancy = Pregnancy.mock,
            popBackStack = {},
            deletePregnancy = {},
            navigateToEditPregnancy = {}
        )
    }
}
