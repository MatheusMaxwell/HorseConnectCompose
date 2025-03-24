package br.com.horseconnect.impl.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.manager.SessionManager
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.Farm
import br.com.horseconnect.impl.domain.usecase.GetAnimalsUseCase
import br.com.horseconnect.impl.domain.usecase.GetUserFarmUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getUserFarmUseCase: GetUserFarmUseCase,
    private val getAnimalsUseCase: GetAnimalsUseCase
): ViewModel() {

    private val _homeState: MutableStateFlow<HomeViewState> = MutableStateFlow(HomeViewState.Initial)
    val homeState: StateFlow<HomeViewState> get() = _homeState

    fun loadInitialData(){
        getUserFarm {
            getAnimals()
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

    private fun getAnimals() {
        viewModelScope.launch {
            when(val result = getAnimalsUseCase.invoke()) {
                is ResponseResult.Success -> {
                    _homeState.tryEmit(HomeViewState.AnimalsPopulated(result.data))
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
    var animals: List<Animal>? = null
) {
    fun isCompleted(): Boolean {
        return farm != null && animals != null
    }
}

sealed class HomeViewState() {
    data object Initial : HomeViewState()
    data object Loading : HomeViewState()
    data class Error(val msg: String) : HomeViewState()
    data class FarmPopulated(val data: Farm) : HomeViewState()
    data class AnimalsPopulated(val data: List<Animal>) : HomeViewState()
}