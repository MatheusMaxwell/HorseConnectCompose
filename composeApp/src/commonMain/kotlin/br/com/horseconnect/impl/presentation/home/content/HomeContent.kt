package br.com.horseconnect.impl.presentation.home.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.presentation.home.HomeData
import br.com.horseconnect.impl.presentation.home.HomeViewState
import br.com.horseconnect.impl.presentation.home.animals.AnimalsContent
import br.com.horseconnect.impl.presentation.home.toolbar.Toolbar
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.loading.LoadingDialogManager
import br.com.horseconnect.theme.dimens.fontsize.FontSize
import br.com.horseconnect.theme.dimens.padding.Padding
import br.com.horseconnect.theme.dimens.size.CustomSize
import horseconnect.composeapp.generated.resources.Res
import horseconnect.composeapp.generated.resources.baseline_more_vert_24
import horseconnect.composeapp.generated.resources.icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeContent(
    loadInitialData: () -> Unit,
    loadingDialogManager: LoadingDialogManager,
    viewState: HomeViewState,
    onClickAnimals: () -> Unit
) {
    val toaster = LocalToasterDialog.current
    var homeData by remember { mutableStateOf(HomeData()) }

    LaunchedEffect(Unit) {
        loadingDialogManager.show()
        loadInitialData.invoke()
    }

    LaunchedEffect(homeData) {
        if(homeData.isCompleted()) {
            loadingDialogManager.hide()
        }
    }

    LaunchedEffect(viewState) {
        when(viewState) {
            HomeViewState.Initial -> Unit
            HomeViewState.Loading -> {
                loadingDialogManager.show()
            }
            is HomeViewState.FarmPopulated -> {
                homeData = homeData.copy(farm = viewState.data)
            }
            is HomeViewState.AnimalsPopulated -> {
                homeData = homeData.copy(animals = viewState.data)
            }
            is HomeViewState.Error -> {
                loadingDialogManager.hide()
                toaster.show(viewState.msg)
            }
        }
    }

    Scaffold(
        topBar = {
            Toolbar(homeData.farm?.name.orEmpty(),)
        },
        backgroundColor = HorseColor.background
    ){
        Column {
            AnimalsContent(homeData.animals.orEmpty(), onClickAnimals)
        }
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    HomeContent(
        loadInitialData = {},
        loadingDialogManager = LoadingDialogManager(),
        viewState = HomeViewState.Initial,
        onClickAnimals = {}
    )
}