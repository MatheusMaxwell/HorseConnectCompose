package br.com.horseconnect.impl.presentation.animals.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.presentation.animals.detail.content.AnimalDetailContent
import br.com.horseconnect.impl.presentation.animals.list.AnimalsListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AnimalDetailScreen(
    animalId: String,
    popBackStack: () -> Unit,
    navigateToGenealogy: (animalId: String, animalName: String) -> Unit
){
    val viewModel = koinViewModel<AnimalDetailViewModel>()
    val viewState by viewModel.animalState.collectAsStateWithLifecycle()

    AnimalDetailContent(
        viewState = viewState,
        getCurrentAnimal = { viewModel.getAnimalById(animalId) },
        popBackStack = popBackStack,
        navigateToGenealogy = navigateToGenealogy
    )
}