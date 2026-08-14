package br.com.horseconnect.impl.presentation.animals.genealogy.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.impl.domain.model.Genealogy
import br.com.horseconnect.impl.presentation.animals.genealogy.GenealogyViewState
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.button.PrimaryButton
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import br.com.horseconnect.theme.components.toolbar.Toolbar
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.shape.Shape
import br.com.horseconnect.theme.dimens.size.CustomSize
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun GenealogyContent(
    viewState: GenealogyViewState,
    animalName: String,
    popBackStack: () -> Unit,
    onSave: (Genealogy) -> Unit = {}
){
    var genealogy: Genealogy? by remember { mutableStateOf(null) }
    var isEditing by remember { mutableStateOf(false) }
    val loading = LocalLoadingDialog.current
    val toaster = LocalToasterDialog.current

    LaunchedEffect(viewState) {
        when(viewState) {
            is GenealogyViewState.Populated -> {
                loading.hide()
                genealogy = viewState.data
            }
            is GenealogyViewState.UpdateSuccess -> {
                loading.hide()
                genealogy = viewState.data
                isEditing = false
                toaster.show("Genealogia atualizada.")
            }
            GenealogyViewState.Saving -> {
                loading.show()
            }
            is GenealogyViewState.Error -> {
                loading.hide()
                toaster.show(viewState.msg)
                if (genealogy == null) {
                    popBackStack.invoke()
                }
            }
            GenealogyViewState.Initial -> Unit
            GenealogyViewState.Loading -> {
                loading.show()
            }
        }
    }

    Scaffold(
        topBar = {
            Toolbar(
                title = animalName,
                onNavigationAction = {
                    popBackStack.invoke()
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .size(CustomSize.toolbarActionIconSize)
                            .clickable { isEditing = !isEditing },
                    ) {
                        Icon(
                            modifier = Modifier.align(Alignment.Center),
                            imageVector = if (isEditing) Icons.Default.Close else Icons.Default.Edit,
                            contentDescription = if (isEditing) "Cancelar edição" else "Editar",
                            tint = HorseColor.accent
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (isEditing) {
                Box(modifier = Modifier.fillMaxWidth().padding(Padding.medium)) {
                    PrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Salvar"
                    ) {
                        genealogy?.let { onSave.invoke(it) }
                    }
                }
            }
        },
        backgroundColor = HorseColor.background
    ) { innerPadding ->
        genealogy?.let { currentGenealogy ->
            BoxWithConstraints(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                val availableHeight = maxHeight
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    GrowToFitOrScroll(
                        minHeight = availableHeight,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Padding.medium),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                    Column(
                        modifier = Modifier.fillMaxHeight().weight(1f),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        GenealogyItem(
                            label = "Pai",
                            enabled = isEditing,
                            text = currentGenealogy.father,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(father = it)
                            }
                        )
                        GenealogyItem(
                            label = "Mãe",
                            enabled = isEditing,
                            text = currentGenealogy.mother,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(mother = it)
                            }
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxHeight().weight(1f),
                        verticalArrangement = Arrangement.SpaceAround
                    ){
                        GenealogyItem(
                            label = "Avô Paterno",
                            enabled = isEditing,
                            text = currentGenealogy.fatherGrandFather,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(fatherGrandFather = it)
                            }
                        )
                        GenealogyItem(
                            label = "Avó Paterna",
                            enabled = isEditing,
                            text = currentGenealogy.fatherGrandMother,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(fatherGrandMother = it)
                            }
                        )
                        GenealogyItem(
                            label = "Avô Materno",
                            enabled = isEditing,
                            text = currentGenealogy.motherGrandFather,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(motherGrandFather = it)
                            }
                        )
                        GenealogyItem(
                            label = "Avó Materna",
                            enabled = isEditing,
                            text = currentGenealogy.motherGrandMother,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(motherGrandMother = it)
                            }
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxHeight().weight(1f),
                        verticalArrangement = Arrangement.SpaceAround
                    ){
                        GenealogyItem(
                            label = "Bisavô (pai do avô paterno)",
                            enabled = isEditing,
                            text = currentGenealogy.fatherGrandFatherGreatGrandFather,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(fatherGrandFatherGreatGrandFather = it)
                            }
                        )
                        GenealogyItem(
                            label = "Bisavó (mãe do avô paterno)",
                            enabled = isEditing,
                            text = currentGenealogy.fatherGrandFatherGreatGrandMother,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(fatherGrandFatherGreatGrandMother = it)
                            }
                        )
                        GenealogyItem(
                            label = "Bisavô (pai da avó paterna)",
                            enabled = isEditing,
                            text = currentGenealogy.fatherGrandMotherGreatGrandFather,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(fatherGrandMotherGreatGrandFather = it)
                            }
                        )
                        GenealogyItem(
                            label = "Bisavó (mãe da avó paterna)",
                            enabled = isEditing,
                            text = currentGenealogy.fatherGrandMotherGreatGrandMother,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(fatherGrandMotherGreatGrandMother = it)
                            }
                        )
                        GenealogyItem(
                            label = "Bisavô (pai do avô materno)",
                            enabled = isEditing,
                            text = currentGenealogy.motherGrandFatherGreatGrandFather,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(motherGrandFatherGreatGrandFather = it)
                            }
                        )
                        GenealogyItem(
                            label = "Bisavó (mãe do avô materno)",
                            enabled = isEditing,
                            text = currentGenealogy.motherGrandFatherGreatGrandMother,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(motherGrandFatherGreatGrandMother = it)
                            }
                        )
                        GenealogyItem(
                            label = "Bisavô (pai da avó materna)",
                            enabled = isEditing,
                            text = currentGenealogy.motherGrandMotherGreatGrandFather,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(motherGrandMotherGreatGrandFather = it)
                            }
                        )
                        GenealogyItem(
                            label = "Bisavó (mãe da avó materna)",
                            enabled = isEditing,
                            text = currentGenealogy.motherGrandMotherGreatGrandMother,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(motherGrandMotherGreatGrandMother = it)
                            }
                        )
                    }
                    }
                    }
                }
            }
        }
    }
}

/**
 * Sizes [content] to at least [minHeight] (so weight/Arrangement.SpaceAround-based children can
 * fill and spread across the available screen space, matching the pre-scroll layout), but grows
 * beyond it when the content's natural height needs more room, instead of clipping it — letting
 * an ancestor `verticalScroll` pick up the overflow.
 */
@Composable
private fun GrowToFitOrScroll(
    minHeight: Dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val measurable = measurables.first()
        val minHeightPx = minHeight.roundToPx()
        val intrinsicHeight = measurable.maxIntrinsicHeight(constraints.maxWidth)
        val resolvedHeight = maxOf(minHeightPx, intrinsicHeight)
        val placeable = measurable.measure(
            constraints.copy(minHeight = resolvedHeight, maxHeight = resolvedHeight)
        )
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
}

@Composable
private fun GenealogyItem(
    label: String,
    text: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(Padding.medium),
        elevation = 0.dp,
        shape = Shape.small,
        backgroundColor = HorseColor.surface,
        border = BorderStroke(CustomSize.borderThin, HorseColor.border)
    ) {
        Row(
            modifier = Modifier.padding(Padding.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text.ifEmpty { "-" },
                style = TextStyle(fontSize = 12.sp),
                color = HorseColor.textPrimary,
                modifier = Modifier.weight(1f)
            )
            if (enabled) {
                Spacer(modifier = Modifier.width(Padding.small))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar $label",
                    tint = HorseColor.accent,
                    modifier = Modifier
                        .size(CustomSize.toolbarActionIconSize)
                        .clickable { showDialog = true }
                )
            }
        }
    }

    if (showDialog) {
        GenealogyEditDialog(
            label = label,
            initialValue = text,
            onDismiss = { showDialog = false },
            onConfirm = {
                onValueChange(it)
                showDialog = false
            }
        )
    }
}

@Composable
private fun GenealogyEditDialog(
    label: String,
    initialValue: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var value by remember(initialValue) { mutableStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        backgroundColor = HorseColor.surface,
        title = {
            Text(label, color = HorseColor.textPrimary, style = TextStyle(fontWeight = FontWeight.Bold))
        },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxWidth().padding(top = Padding.small)
            )
        },
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(Padding.medium),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Cancelar",
                    color = HorseColor.textSecondary,
                    modifier = Modifier.clickable { onDismiss.invoke() }
                )
                Spacer(modifier = Modifier.width(Padding.medium))
                Text(
                    text = "Salvar",
                    color = HorseColor.accent,
                    modifier = Modifier.clickable { onConfirm.invoke(value) }
                )
            }
        }
    )
}

@Preview
@Composable
private fun GenealogyContentPreview() {
    HorseTheme {
        GenealogyContent(
            viewState = GenealogyViewState.Populated(Genealogy.mock),
            animalName = "Monarca de Alcatéia",
            popBackStack = {}
        )
    }
}
