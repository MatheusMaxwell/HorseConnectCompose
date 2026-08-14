package br.com.horseconnect.impl.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.manager.SessionManager
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.Farm
import br.com.horseconnect.impl.domain.model.FinanceEntry
import br.com.horseconnect.impl.domain.model.Notification
import br.com.horseconnect.impl.domain.model.Pregnancy
import br.com.horseconnect.impl.domain.model.Vaccine
import br.com.horseconnect.impl.domain.usecase.animal.GetAnimalsUseCase
import br.com.horseconnect.impl.domain.usecase.finance.GetFinanceEntriesUseCase
import br.com.horseconnect.impl.domain.usecase.notification.CreateNotificationUseCase
import br.com.horseconnect.impl.domain.usecase.notification.GetNotificationsUseCase
import br.com.horseconnect.impl.domain.usecase.pregnancy.GetPregnanciesUseCase
import br.com.horseconnect.impl.domain.usecase.user.DoLogoutUseCase
import br.com.horseconnect.impl.domain.usecase.user.GetUserFarmUseCase
import br.com.horseconnect.impl.domain.usecase.vaccine.GetVaccinesUseCase
import br.com.horseconnect.impl.domain.util.generateDueNotifications
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class HomeViewModel(
    private val getUserFarmUseCase: GetUserFarmUseCase,
    private val getAnimalsUseCase: GetAnimalsUseCase,
    private val getPregnanciesUseCase: GetPregnanciesUseCase,
    private val getVaccinesUseCase: GetVaccinesUseCase,
    private val getFinanceEntriesUseCase: GetFinanceEntriesUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val createNotificationUseCase: CreateNotificationUseCase,
    private val doLogoutUseCase: DoLogoutUseCase
): ViewModel() {

    private val _homeState: MutableStateFlow<HomeViewState> = MutableStateFlow(HomeViewState.Initial)
    val homeState: StateFlow<HomeViewState> get() = _homeState

    private var loadedPregnancies: List<Pregnancy> = emptyList()
    private var loadedVaccines: List<Vaccine> = emptyList()
    private var loadedFinanceEntries: List<FinanceEntry> = emptyList()

    fun loadInitialData(){
        getUserFarm {
            getAnimals {
                getPregnancies {
                    getVaccines {
                        getFinanceEntries {
                            checkDueNotifications()
                        }
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            when(doLogoutUseCase.invoke()) {
                is ResponseResult.Success -> {
                    _homeState.tryEmit(HomeViewState.LogoutSuccess)
                }
                is ResponseResult.Error -> {
                    _homeState.tryEmit(HomeViewState.LogoutSuccess)
                }
            }
        }
    }

    private fun getUserFarm(onResult: () -> Unit) {
        viewModelScope.launch {
            when(val result = getUserFarmUseCase.invoke()) {
                is ResponseResult.Success -> {
                    SessionManager.farm = result.data
                    _homeState.tryEmit(HomeViewState.FarmPopulated(result.data))
                    onResult.invoke()
                }
                is ResponseResult.Error -> {
                    _homeState.tryEmit(HomeViewState.Error(result.resultError.msg))
                }
            }
        }
    }

    private fun getAnimals(onResult: () -> Unit) {
        viewModelScope.launch {
            when(val result = getAnimalsUseCase.invoke()) {
                is ResponseResult.Success -> {
                    _homeState.update {
                        HomeViewState.AnimalsPopulated(result.data)
                    }
                    onResult.invoke()
                }
                is ResponseResult.Error -> {
                    _homeState.tryEmit(HomeViewState.Error(result.resultError.msg))
                }
            }
        }
    }

    private fun getPregnancies(onResult: () -> Unit) {
        viewModelScope.launch {
            when(val result = getPregnanciesUseCase.invoke()) {
                is ResponseResult.Success -> {
                    loadedPregnancies = result.data
                    _homeState.update {
                        HomeViewState.PregnanciesPopulated(result.data)
                    }
                    onResult.invoke()
                }
                is ResponseResult.Error -> {
                    _homeState.tryEmit(HomeViewState.Error(result.resultError.msg))
                }
            }
        }
    }

    private fun getVaccines(onResult: () -> Unit) {
        viewModelScope.launch {
            when(val result = getVaccinesUseCase.invoke()) {
                is ResponseResult.Success -> {
                    loadedVaccines = result.data
                    _homeState.update {
                        HomeViewState.VaccinesPopulated(result.data)
                    }
                    onResult.invoke()
                }
                is ResponseResult.Error -> {
                    _homeState.tryEmit(HomeViewState.Error(result.resultError.msg))
                }
            }
        }
    }

    private fun getFinanceEntries(onResult: () -> Unit) {
        viewModelScope.launch {
            when(val result = getFinanceEntriesUseCase.invoke()) {
                is ResponseResult.Success -> {
                    loadedFinanceEntries = result.data
                    _homeState.update {
                        HomeViewState.FinanceEntriesPopulated(result.data)
                    }
                    onResult.invoke()
                }
                is ResponseResult.Error -> {
                    _homeState.tryEmit(HomeViewState.Error(result.resultError.msg))
                }
            }
        }
    }

    private fun checkDueNotifications() {
        viewModelScope.launch {
            when(val existingResult = getNotificationsUseCase.invoke()) {
                is ResponseResult.Success -> {
                    val dueNotifications = generateDueNotifications(
                        userId = SessionManager.user?.id.orEmpty(),
                        existingNotifications = existingResult.data,
                        pregnancies = loadedPregnancies,
                        vaccines = loadedVaccines,
                        financeEntries = loadedFinanceEntries
                    )
                    if (dueNotifications.isEmpty()) {
                        _homeState.update {
                            HomeViewState.NotificationsPopulated(existingResult.data)
                        }
                    } else {
                        dueNotifications.forEach { createNotificationUseCase.invoke(it) }
                        getNotifications()
                    }
                }
                is ResponseResult.Error -> {
                    _homeState.tryEmit(HomeViewState.Error(existingResult.resultError.msg))
                }
            }
        }
    }

    private fun getNotifications() {
        viewModelScope.launch {
            when(val result = getNotificationsUseCase.invoke()) {
                is ResponseResult.Success -> {
                    _homeState.update {
                        HomeViewState.NotificationsPopulated(result.data)
                    }
                }
                is ResponseResult.Error -> {
                    _homeState.tryEmit(HomeViewState.Error(result.resultError.msg))
                }
            }
        }
    }
}

data class HomeData(
    var farm: Farm? = null,
    var animals: List<Animal>? = null,
    var pregnancies: List<Pregnancy>? = null,
    var vaccines: List<Vaccine>? = null,
    var financeEntries: List<FinanceEntry>? = null,
    var notifications: List<Notification>? = null
) {
    fun isCompleted(): Boolean {
        return farm != null && animals != null && pregnancies != null &&
                vaccines != null && financeEntries != null && notifications != null
    }
}

sealed class HomeViewState() {
    data object Initial : HomeViewState()
    data object Loading : HomeViewState()
    data class Error(val msg: String) : HomeViewState()
    data class FarmPopulated(val data: Farm) : HomeViewState()
    data class AnimalsPopulated(val data: List<Animal>) : HomeViewState()
    data class PregnanciesPopulated(val data: List<Pregnancy>) : HomeViewState()
    data class VaccinesPopulated(val data: List<Vaccine>) : HomeViewState()
    data class FinanceEntriesPopulated(val data: List<FinanceEntry>) : HomeViewState()
    data class NotificationsPopulated(val data: List<Notification>) : HomeViewState()
    data object LogoutSuccess : HomeViewState()
}
