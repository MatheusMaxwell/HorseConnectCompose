package br.com.horseconnect.impl.presentation.animals.genealogy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.impl.presentation.animals.genealogy.content.GenealogyContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GenealogyScreen(
    animalId: String,
    animalName: String,
    popBackStack: () -> Unit
){
    val viewModel = koinViewModel<GenealogyViewModel>()
    val viewState by viewModel.genealogyState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getGenealogyByAnimalId(animalId)
    }

    GenealogyContent(
        viewState = viewState,
        animalName = animalName,
        popBackStack = popBackStack
    )
}