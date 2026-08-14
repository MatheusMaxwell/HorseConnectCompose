package br.com.horseconnect.impl.presentation.pregnancies.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.impl.domain.model.Pregnancy
import br.com.horseconnect.impl.presentation.pregnancies.register.content.PregnancyRegisterContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PregnancyRegisterScreen(
    currentPregnancy: Pregnancy?,
    popBackStack: () -> Unit
) {
    val viewModel = koinViewModel<PregnancyRegisterViewModel>()
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val animals by viewModel.animals.collectAsStateWithLifecycle()

    PregnancyRegisterContent(
        viewState = viewState,
        currentPregnancy = currentPregnancy,
        animals = animals,
        loadAnimals = viewModel::loadAnimals,
        onRegister = viewModel::registerPregnancy,
        onUpdate = viewModel::updatePregnancy,
        popBackStack = popBackStack
    )
}
