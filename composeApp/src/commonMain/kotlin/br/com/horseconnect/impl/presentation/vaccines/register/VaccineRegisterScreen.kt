package br.com.horseconnect.impl.presentation.vaccines.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.impl.domain.model.Vaccine
import br.com.horseconnect.impl.presentation.vaccines.register.content.VaccineRegisterContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VaccineRegisterScreen(
    vaccines: List<Vaccine>? = null,
    popBackStack: () -> Unit
) {
    val viewModel = koinViewModel<VaccineRegisterViewModel>()
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val animals by viewModel.animals.collectAsStateWithLifecycle()

    VaccineRegisterContent(
        viewState = viewState,
        currentGroup = vaccines,
        animals = animals,
        loadAnimals = { viewModel.loadAnimals() },
        onSubmit = { appliedVaccines ->
            viewModel.submit(appliedVaccines)
        },
        onUpdateGroup = { toCreate, toUpdate, toDeleteIds ->
            viewModel.submitGroupEdit(toCreate, toUpdate, toDeleteIds)
        },
        popBackStack = popBackStack
    )
}
