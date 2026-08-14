package br.com.horseconnect.impl.presentation.pregnancies.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.Pregnancy
import br.com.horseconnect.impl.domain.usecase.animal.GetAnimalsUseCase
import br.com.horseconnect.impl.domain.usecase.pregnancy.RegisterPregnancyUseCase
import br.com.horseconnect.impl.domain.usecase.pregnancy.UpdatePregnancyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PregnancyRegisterViewModel(
    private val getAnimalsUseCase: GetAnimalsUseCase,
    private val registerPregnancyUseCase: RegisterPregnancyUseCase,
    private val updatePregnancyUseCase: UpdatePregnancyUseCase
): ViewModel() {

    private val _viewState: MutableStateFlow<PregnancyRegisterViewState> = MutableStateFlow(
        PregnancyRegisterViewState.Initial
    )
    val viewState: StateFlow<PregnancyRegisterViewState> get() = _viewState

    private val _animals: MutableStateFlow<List<Animal>> = MutableStateFlow(emptyList())
    val animals: StateFlow<List<Animal>> get() = _animals

    fun loadAnimals() {
        viewModelScope.launch {
            when(val result = getAnimalsUseCase.invoke()) {
                is ResponseResult.Success -> {
                    _animals.tryEmit(result.data)
                }
                is ResponseResult.Error -> {
                    _viewState.tryEmit(PregnancyRegisterViewState.Error(result.resultError.msg))
                }
            }
        }
    }

    fun registerPregnancy(pregnancy: Pregnancy) {
        _viewState.tryEmit(PregnancyRegisterViewState.Loading)
        viewModelScope.launch {
            when(val result = registerPregnancyUseCase.invoke(pregnancy)) {
                is ResponseResult.Success -> {
                    _viewState.tryEmit(PregnancyRegisterViewState.RegisterSuccess)
                }
                is ResponseResult.Error -> {
                    _viewState.tryEmit(PregnancyRegisterViewState.Error(result.resultError.msg))
                }
            }
        }
    }

    fun updatePregnancy(pregnancy: Pregnancy) {
        _viewState.tryEmit(PregnancyRegisterViewState.Loading)
        viewModelScope.launch {
            when(val result = updatePregnancyUseCase.invoke(pregnancy)) {
                is ResponseResult.Success -> {
                    _viewState.tryEmit(PregnancyRegisterViewState.RegisterSuccess)
                }
                is ResponseResult.Error -> {
                    _viewState.tryEmit(PregnancyRegisterViewState.Error(result.resultError.msg))
                }
            }
        }
    }
}

sealed class PregnancyRegisterViewState {
    data object Initial : PregnancyRegisterViewState()
    data object Loading : PregnancyRegisterViewState()
    data class Error(val msg: String) : PregnancyRegisterViewState()
    data object RegisterSuccess : PregnancyRegisterViewState()
}
