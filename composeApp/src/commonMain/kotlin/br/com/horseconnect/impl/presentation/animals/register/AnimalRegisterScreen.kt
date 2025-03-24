package br.com.horseconnect.impl.presentation.animals.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.impl.presentation.animals.register.content.AnimalRegisterContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AnimalRegisterScreen(
    popBackStack: () -> Unit
) {
    val viewModel = koinViewModel<AnimalRegisterViewModel>()
    val viewState by viewModel.animalRegisterState.collectAsStateWithLifecycle()

    AnimalRegisterContent(
        viewState = viewState,
        onRegister = { animal, image ->
            image?.let {
                viewModel.uploadImage(it, animal)
            } ?: run {
                viewModel.registerAnimal(animal)
            }
        },
        popBackStack = popBackStack
    )
}