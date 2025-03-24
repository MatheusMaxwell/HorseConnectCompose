package br.com.horseconnect.impl.presentation.animals.genealogy.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.impl.domain.model.Genealogy
import br.com.horseconnect.impl.presentation.animals.detail.AnimalDetailViewState
import br.com.horseconnect.impl.presentation.animals.genealogy.GenealogyViewState
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import br.com.horseconnect.theme.components.toolbar.Toolbar
import br.com.horseconnect.theme.dimens.padding.Padding
import horseconnect.composeapp.generated.resources.Res
import horseconnect.composeapp.generated.resources.baseline_arrow_back_24
import org.jetbrains.compose.resources.painterResource

@Composable
fun GenealogyContent(
    viewState: GenealogyViewState,
    animalName: String,
    popBackStack: () -> Unit
){
    var genealogy: Genealogy? by mutableStateOf(null)
    val loading = LocalLoadingDialog.current
    val toaster = LocalToasterDialog.current

    LaunchedEffect(viewState) {
        when(viewState) {
            is GenealogyViewState.Populated -> {
                loading.hide()
                genealogy = viewState.data
            }
            is GenealogyViewState.Error -> {
                loading.hide()
                toaster.show(viewState.msg)
                popBackStack.invoke()
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
                onNavigationAction = {
                    popBackStack.invoke()
                }
            )
        },
        backgroundColor = HorseColor.background
    ) {
        genealogy?.let { currentGenealogy ->
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(Padding.medium),
                    text = animalName,
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.fillMaxSize().padding(Padding.medium),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight().weight(1f),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        GenealogyItem(
                            currentGenealogy.father,
                            enabled = false,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(father = it)
                            }
                        )
                        GenealogyItem(
                            currentGenealogy.mother,
                            enabled = false,
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
                            currentGenealogy.fatherGrandFather,
                            enabled = false,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(fatherGrandFather = it)
                            }
                        )
                        GenealogyItem(
                            currentGenealogy.fatherGrandMother,
                            enabled = false,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(fatherGrandMother = it)
                            }
                        )
                        GenealogyItem(
                            currentGenealogy.motherGrandFather,
                            enabled = false,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(motherGrandFather = it)
                            }
                        )
                        GenealogyItem(
                            currentGenealogy.motherGrandMother,
                            enabled = false,
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
                            currentGenealogy.fatherGrandFatherGreatGrandFather,
                            enabled = false,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(fatherGrandFatherGreatGrandFather = it)
                            }
                        )
                        GenealogyItem(
                            currentGenealogy.fatherGrandFatherGreatGrandMother,
                            enabled = false,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(fatherGrandFatherGreatGrandMother = it)
                            }
                        )
                        GenealogyItem(
                            currentGenealogy.fatherGrandMotherGreatGrandFather,
                            enabled = false,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(motherGrandFatherGreatGrandFather = it)
                            }
                        )
                        GenealogyItem(
                            currentGenealogy.fatherGrandMotherGreatGrandFather,
                            enabled = false,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(motherGrandMotherGreatGrandMother = it)
                            }
                        )
                        GenealogyItem(
                            currentGenealogy.motherGrandFatherGreatGrandFather,
                            enabled = false,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(motherGrandFatherGreatGrandFather = it)
                            }
                        )
                        GenealogyItem(
                            currentGenealogy.motherGrandFatherGreatGrandMother,
                            enabled = false,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(motherGrandFatherGreatGrandMother = it)
                            }
                        )
                        GenealogyItem(
                            currentGenealogy.motherGrandMotherGreatGrandFather,
                            enabled = false,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(motherGrandMotherGreatGrandFather = it)
                            }
                        )
                        GenealogyItem(
                            currentGenealogy.motherGrandMotherGreatGrandFather,
                            enabled = false,
                            onValueChange = {
                                genealogy = currentGenealogy.copy(motherGrandMotherGreatGrandFather = it)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GenealogyItem(
    text: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.padding(Padding.medium),
        elevation = 6.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.White
    ) {
        TextField(
            value = text,
            textStyle = TextStyle(fontSize = 12.sp),
            onValueChange = onValueChange,
            enabled = enabled,
            maxLines = 3,
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