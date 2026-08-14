package br.com.horseconnect.impl.presentation.vaccines.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.impl.domain.model.Vaccine
import br.com.horseconnect.impl.presentation.vaccines.list.content.VaccineListContent
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VaccineListScreen(
    onGroupClick: (vaccines: List<Vaccine>) -> Unit,
    popBackStack: () -> Unit,
    navigateToRegister: () -> Unit
) {
    val viewModel = koinViewModel<VaccineListViewModel>()
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val loadingDialog = LocalLoadingDialog.current

    LaunchedEffect(Unit) {
        viewModel.getVaccines()
    }

    VaccineListContent(
        viewState = viewState,
        loadingDialogManager = loadingDialog,
        onSearchQueryChanged = { viewModel.getSearchVaccines(it) },
        onNameFilterChanged = { viewModel.getVaccinesFiltered(it) },
        onGroupClick = onGroupClick,
        popBackStack = popBackStack,
        navigateToRegister = navigateToRegister
    )
}
