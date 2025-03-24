package br.com.horseconnect.impl.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.User
import br.com.horseconnect.impl.presentation.home.content.HomeContent
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun HomeScreen(
    onClickAnimals: () -> Unit
) {
    val homeViewModel = koinViewModel<HomeViewModel>()
    val homeViewState by homeViewModel.homeState.collectAsStateWithLifecycle()
    val loadingDialog = LocalLoadingDialog.current

    HomeContent(
        loadInitialData = homeViewModel::loadInitialData,
        loadingDialogManager = loadingDialog,
        viewState = homeViewState,
        onClickAnimals = onClickAnimals
    )
}