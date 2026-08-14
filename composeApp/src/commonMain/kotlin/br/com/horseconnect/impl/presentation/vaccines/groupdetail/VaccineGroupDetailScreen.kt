package br.com.horseconnect.impl.presentation.vaccines.groupdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.impl.domain.model.Vaccine
import br.com.horseconnect.impl.presentation.vaccines.groupdetail.content.VaccineGroupDetailContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VaccineGroupDetailScreen(
    vaccines: List<Vaccine>,
    popBackStack: () -> Unit,
    navigateToEdit: (vaccines: List<Vaccine>) -> Unit
) {
    val viewModel = koinViewModel<VaccineGroupDetailViewModel>()
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    VaccineGroupDetailContent(
        viewState = viewState,
        vaccines = vaccines,
        popBackStack = popBackStack,
        deleteGroup = { viewModel.deleteGroup(it) },
        navigateToEdit = navigateToEdit
    )
}
