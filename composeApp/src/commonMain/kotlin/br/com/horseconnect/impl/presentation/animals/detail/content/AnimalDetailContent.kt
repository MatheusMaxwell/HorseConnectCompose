package br.com.horseconnect.impl.presentation.animals.detail.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.Owner
import br.com.horseconnect.impl.presentation.animals.detail.AnimalDetailViewState
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.button.PrimaryButton
import br.com.horseconnect.theme.components.dialog.ConfirmAction
import br.com.horseconnect.theme.components.dialog.CustomDialog
import br.com.horseconnect.theme.components.dialog.DismissAction
import br.com.horseconnect.theme.components.expandablelist.ExpandableList
import br.com.horseconnect.theme.components.expandablelist.Item
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import br.com.horseconnect.theme.components.titlevalue.TitleValue
import br.com.horseconnect.theme.components.toolbar.Toolbar
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.shape.Shape
import coil3.compose.AsyncImage
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AnimalDetailContent(
    viewState: AnimalDetailViewState,
    getCurrentAnimal: () -> Unit,
    popBackStack: () -> Unit,
    navigateToGenealogy: (animalId: String, animalName: String) -> Unit,
    deleteAnimal: (Animal) -> Unit,
    navigateToEditAnimal: (animal: Animal) -> Unit
) {
    var animal: Animal? by remember { mutableStateOf(null) }
    val toaster = LocalToasterDialog.current
    val loading = LocalLoadingDialog.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        getCurrentAnimal.invoke()
    }

    LaunchedEffect(viewState) {
        when(viewState) {
            is AnimalDetailViewState.Populated -> {
                loading.hide()
                animal = viewState.data
            }
            is AnimalDetailViewState.Error -> {
                loading.hide()
                toaster.show(viewState.msg)
            }
            AnimalDetailViewState.Initial -> Unit
            AnimalDetailViewState.Loading -> {
                loading.show()
            }
            AnimalDetailViewState.DeletedSuccess -> {
                loading.hide()
                toaster.show("Animal deletado com sucesso.")
                popBackStack.invoke()
            }
        }
    }

    DeleteAnimalDialog(
        showDeleteDialog = showDeleteDialog,
        onDismiss = { showDeleteDialog = false },
        onConfirmDelete = {
            deleteAnimal.invoke(animal!!)
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
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = Padding.medium)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = animal?.name.orEmpty(),
                    color = HorseColor.textPrimary,
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
                Icon(
                    modifier = Modifier.clickable {
                        animal?.let { navigateToEditAnimal.invoke(it) }
                    },
                    imageVector = Icons.Filled.Edit,
                    tint = HorseColor.accent,
                    contentDescription = "Editar"
                )
            }
            AsyncImage(
                model = animal?.imageUrl,
                contentDescription = animal?.name,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxHeight(0.3f)
                    .clip(Shape.medium)
                    .background(HorseColor.surfaceVariant)
            )
            TitleValue("Nascimento", value = DateUtil.getDateFormatter(animal?.birthDate.orEmpty()))
            TitleValue("Sexo", value = animal?.sex.orEmpty())
            TitleValue("Pelagem", value = animal?.coat.orEmpty())
            TitleValue("Vivo", value = if (animal?.isLive == true) "Sim" else "Não")
            TitleValue("Propriedade", value = "${animal?.ownerOf}%")
            Owners(owners = animal?.otherOwners.orEmpty())
            Spacer(modifier = Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth().padding(Padding.medium),
                    text = "Genealogia"
                ){
                    navigateToGenealogy.invoke(animal?.id.orEmpty(), animal?.name.orEmpty())
                }
                Text(
                    text = "Excluir animal",
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
fun DeleteAnimalDialog(
    showDeleteDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    CustomDialog(
        visible = showDeleteDialog,
        title = "Excluir animal",
        message = "Tem certeza que deseja excluir este animal?",
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

@Composable
private fun Owners(owners: List<Owner>) {
    if(owners.isNotEmpty()) {
        ExpandableList(
            modifier = Modifier.padding(top = Padding.medium),
            title = "Outros proprietários",
            items = owners.map { Item(it.name, "${it.ownerOf}%") }
        )
    }
}

@Preview
@Composable
private fun AnimalDetailContentPreview() {
    HorseTheme {
        AnimalDetailContent(
            viewState = AnimalDetailViewState.Populated(
                Animal.mock
            ),
            getCurrentAnimal = {},
            popBackStack = {},
            navigateToGenealogy = { _, _ -> },
            deleteAnimal = {},
            navigateToEditAnimal = {}
        )
    }
}