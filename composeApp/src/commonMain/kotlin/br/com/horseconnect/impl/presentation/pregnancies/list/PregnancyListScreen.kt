package br.com.horseconnect.impl.presentation.pregnancies.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.impl.domain.model.Pregnancy
import br.com.horseconnect.impl.presentation.pregnancies.list.content.PregnancyListContent
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PregnancyListScreen(
    onItemClick: (pregnancy: Pregnancy) -> Unit,
    popBackStack: () -> Unit,
    navigateToRegister: () -> Unit
) {
    val viewModel = koinViewModel<PregnancyListViewModel>()
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val loadingDialog = LocalLoadingDialog.current

    LaunchedEffect(Unit) {
        viewModel.getPregnancies()
    }

    PregnancyListContent(
        viewState = viewState,
        loadingDialogManager = loadingDialog,
        onSearchQueryChanged = { viewModel.getSearchPregnancies(it) },
        onStatusFilterChanged = { viewModel.getPregnanciesFiltered(it) },
        onItemClick = onItemClick,
        popBackStack = popBackStack,
        navigateToRegister = navigateToRegister
    )
}
