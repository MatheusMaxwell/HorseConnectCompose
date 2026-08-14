package br.com.horseconnect.impl.presentation.reports

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.impl.presentation.reports.content.ReportsContent
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReportsScreen() {
    val viewModel = koinViewModel<ReportsViewModel>()
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val loadingDialog = LocalLoadingDialog.current

    LaunchedEffect(Unit) {
        viewModel.loadReports()
    }

    ReportsContent(
        viewState = viewState,
        loadingDialogManager = loadingDialog
    )
}
