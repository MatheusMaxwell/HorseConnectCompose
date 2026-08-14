package br.com.horseconnect.impl.presentation.pregnancies.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.impl.domain.model.Pregnancy
import br.com.horseconnect.impl.presentation.pregnancies.detail.content.PregnancyDetailContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PregnancyDetailScreen(
    pregnancy: Pregnancy,
    popBackStack: () -> Unit,
    navigateToEdit: (pregnancy: Pregnancy) -> Unit
) {
    val viewModel = koinViewModel<PregnancyDetailViewModel>()
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    PregnancyDetailContent(
        viewState = viewState,
        pregnancy = pregnancy,
        popBackStack = popBackStack,
        deletePregnancy = viewModel::deletePregnancy,
        navigateToEditPregnancy = navigateToEdit
    )
}
