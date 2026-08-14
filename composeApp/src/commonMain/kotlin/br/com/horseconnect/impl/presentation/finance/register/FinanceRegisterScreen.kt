package br.com.horseconnect.impl.presentation.finance.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.impl.domain.model.FinanceEntry
import br.com.horseconnect.impl.presentation.finance.register.content.FinanceRegisterContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FinanceRegisterScreen(
    currentEntry: FinanceEntry? = null,
    popBackStack: () -> Unit
) {
    val viewModel = koinViewModel<FinanceRegisterViewModel>()
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    FinanceRegisterContent(
        viewState = viewState,
        currentEntry = currentEntry,
        onRegister = { viewModel.register(it) },
        onUpdate = { viewModel.update(it) },
        popBackStack = popBackStack
    )
}
