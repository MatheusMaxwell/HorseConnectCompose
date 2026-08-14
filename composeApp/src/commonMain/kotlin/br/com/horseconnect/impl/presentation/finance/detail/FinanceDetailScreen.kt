package br.com.horseconnect.impl.presentation.finance.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.impl.domain.model.FinanceEntry
import br.com.horseconnect.impl.presentation.finance.detail.content.FinanceDetailContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FinanceDetailScreen(
    entry: FinanceEntry,
    popBackStack: () -> Unit,
    navigateToEdit: (entry: FinanceEntry) -> Unit
) {
    val viewModel = koinViewModel<FinanceDetailViewModel>()
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    FinanceDetailContent(
        viewState = viewState,
        entry = entry,
        popBackStack = popBackStack,
        deleteEntry = viewModel::deleteEntry,
        navigateToEdit = navigateToEdit
    )
}
