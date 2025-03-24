package br.com.horseconnect.impl.presentation.animals.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.impl.presentation.animals.list.content.AnimalListContent
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AnimalListScreen(
    onItemClick: (animalId: String) -> Unit,
    popBackStack: () -> Unit,
    navigateToRegister: () -> Unit
) {
    val animalsViewModel = koinViewModel<AnimalsListViewModel>()
    val viewState by animalsViewModel.animalsState.collectAsStateWithLifecycle()
    val loadingDialog = LocalLoadingDialog.current

    LaunchedEffect(Unit) {
        animalsViewModel.getAnimals()
    }

    AnimalListContent(
        viewState = viewState,
        loadingDialogManager = loadingDialog,
        onSearchQueryChanged = { animalsViewModel.getSearchAnimals(it) },
        onTypeFilterChanged = { animalsViewModel.getAnimalsFiltered(it) },
        onItemClick = onItemClick,
        popBackStack = popBackStack,
        navigateToRegister = navigateToRegister
    )
}