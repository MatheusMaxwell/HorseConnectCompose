package br.com.horseconnect.impl.presentation.finance.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.impl.domain.model.FinanceEntry
import br.com.horseconnect.impl.presentation.finance.list.content.FinanceListContent
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FinanceListScreen(
    onItemClick: (entry: FinanceEntry) -> Unit,
    popBackStack: () -> Unit,
    navigateToRegister: () -> Unit
) {
    val viewModel = koinViewModel<FinanceListViewModel>()
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val loadingDialog = LocalLoadingDialog.current

    LaunchedEffect(Unit) {
        viewModel.getFinanceEntries()
    }

    FinanceListContent(
        viewState = viewState,
        loadingDialogManager = loadingDialog,
        onSearchQueryChanged = { viewModel.getSearchFinanceEntries(it) },
        onCategoryFilterChanged = { viewModel.getFinanceEntriesFiltered(it) },
        onItemClick = onItemClick,
        popBackStack = popBackStack,
        navigateToRegister = navigateToRegister
    )
}
