package br.com.horseconnect.impl.presentation.home.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.common.util.DateUtil
import br.com.horseconnect.impl.domain.model.Pregnancy
import br.com.horseconnect.impl.domain.util.balance
import br.com.horseconnect.impl.domain.util.toCashEvents
import br.com.horseconnect.impl.presentation.home.HomeData
import br.com.horseconnect.impl.presentation.home.HomeViewState
import br.com.horseconnect.impl.presentation.home.pregnancies.PregnanciesContent
import br.com.horseconnect.impl.presentation.home.quickactions.QuickActionsContent
import br.com.horseconnect.impl.presentation.home.stats.HomeStatsContent
import br.com.horseconnect.impl.presentation.home.toolbar.Toolbar
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.loading.LoadingDialogManager
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeContent(
    loadInitialData: () -> Unit,
    loadingDialogManager: LoadingDialogManager,
    viewState: HomeViewState,
    onClickAnimals: () -> Unit,
    navigateToPregnancyList: () -> Unit,
    navigateToPregnancyDetail: (Pregnancy) -> Unit,
    navigateToVaccineList: () -> Unit,
    navigateToFinance: () -> Unit,
    navigateToNotifications: () -> Unit,
    onLogout: () -> Unit,
    navigateToSplashScreen: () -> Unit
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
            is HomeViewState.PregnanciesPopulated -> {
                homeData = homeData.copy(pregnancies = viewState.data)
            }
            is HomeViewState.VaccinesPopulated -> {
                homeData = homeData.copy(vaccines = viewState.data)
            }
            is HomeViewState.FinanceEntriesPopulated -> {
                homeData = homeData.copy(financeEntries = viewState.data)
            }
            is HomeViewState.NotificationsPopulated -> {
                homeData = homeData.copy(notifications = viewState.data)
            }
            is HomeViewState.Error -> {
                loadingDialogManager.hide()
                toaster.show(viewState.msg)
            }
            is HomeViewState.LogoutSuccess -> {
                navigateToSplashScreen()
            }
        }
    }

    Scaffold(
        topBar = {
            Toolbar(
                farmName = homeData.farm?.name.orEmpty(),
                unreadNotificationsCount = homeData.notifications.orEmpty().count { !it.read },
                onClickNotifications = navigateToNotifications,
                onLogout = onLogout
            )
        },
        backgroundColor = HorseColor.background
    ){
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            val upcomingVaccinesCount = homeData.vaccines?.let { vaccines ->
                val boundary = DateUtil.addDays(DateUtil.now(), 30)
                vaccines.count { it.nextDueDate in DateUtil.now()..boundary }
            }
            val monthBalance = homeData.financeEntries?.let { entries ->
                entries.toCashEvents().filter { DateUtil.isCurrentMonth(it.date) }.balance()
            }

            HomeStatsContent(
                plantelCount = homeData.animals.orEmpty().size,
                pregnanciesCount = homeData.pregnancies.orEmpty().size,
                upcomingVaccinesCount = upcomingVaccinesCount,
                monthBalance = monthBalance,
                onClickPlantel = onClickAnimals,
                onClickGestacoes = navigateToPregnancyList,
                onClickVacinas = navigateToVaccineList,
                onClickFinancas = navigateToFinance
            )
            QuickActionsContent(
                onClickPlantel = onClickAnimals,
                onClickVacinas = navigateToVaccineList,
                onClickCruzamentos = navigateToPregnancyList,
                onClickFinancas = navigateToFinance
            )
            PregnanciesContent(
                pregnancies = homeData.pregnancies.orEmpty(),
                onClickPregnancies = navigateToPregnancyList,
                onClickPregnancy = navigateToPregnancyDetail
            )
        }
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    HorseTheme {
        HomeContent(
            loadInitialData = {},
            loadingDialogManager = LoadingDialogManager(),
            viewState = HomeViewState.Initial,
            onClickAnimals = {},
            navigateToPregnancyList = {},
            navigateToPregnancyDetail = {},
            navigateToVaccineList = {},
            navigateToFinance = {},
            navigateToNotifications = {},
            onLogout = {},
            navigateToSplashScreen = {}
        )
    }
}
