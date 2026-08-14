package br.com.horseconnect.impl.presentation.vaccines.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.Vaccine
import br.com.horseconnect.impl.domain.usecase.animal.GetAnimalsUseCase
import br.com.horseconnect.impl.domain.usecase.vaccine.DeleteVaccineUseCase
import br.com.horseconnect.impl.domain.usecase.vaccine.RegisterVaccineUseCase
import br.com.horseconnect.impl.domain.usecase.vaccine.UpdateVaccineUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VaccineRegisterViewModel(
    private val getAnimalsUseCase: GetAnimalsUseCase,
    private val registerVaccineUseCase: RegisterVaccineUseCase,
    private val updateVaccineUseCase: UpdateVaccineUseCase,
    private val deleteVaccineUseCase: DeleteVaccineUseCase
): ViewModel() {

    private val _viewState: MutableStateFlow<VaccineRegisterViewState> = MutableStateFlow(
        VaccineRegisterViewState.Initial
    )
    val viewState: StateFlow<VaccineRegisterViewState> get() = _viewState

    private val _animals: MutableStateFlow<List<Animal>> = MutableStateFlow(emptyList())
    val animals: StateFlow<List<Animal>> get() = _animals

    fun loadAnimals() {
        viewModelScope.launch {
            when(val result = getAnimalsUseCase.invoke()) {
                is ResponseResult.Success -> {
                    _animals.tryEmit(result.data)
                }
                is ResponseResult.Error -> {
                    _viewState.tryEmit(VaccineRegisterViewState.Error(result.resultError.msg))
                }
            }
        }
    }

    fun submit(appliedVaccines: List<Vaccine>) {
        _viewState.tryEmit(VaccineRegisterViewState.Loading)
        viewModelScope.launch {
            var hasError = false

            appliedVaccines.forEach { vaccine ->
                if (registerVaccineUseCase.invoke(vaccine) is ResponseResult.Error) {
                    hasError = true
                }
            }

            if (hasError) {
                _viewState.tryEmit(VaccineRegisterViewState.Error("Não foi possível salvar todos os registros. Tente novamente."))
            } else {
                _viewState.tryEmit(VaccineRegisterViewState.RegisterSuccess)
            }
        }
    }

    fun submitGroupEdit(toCreate: List<Vaccine>, toUpdate: List<Vaccine>, toDeleteIds: List<String>) {
        _viewState.tryEmit(VaccineRegisterViewState.Loading)
        viewModelScope.launch {
            var hasError = false

            toCreate.forEach { vaccine ->
                if (registerVaccineUseCase.invoke(vaccine) is ResponseResult.Error) {
                    hasError = true
                }
            }

            toUpdate.forEach { vaccine ->
                if (updateVaccineUseCase.invoke(vaccine) is ResponseResult.Error) {
                    hasError = true
                }
            }

            toDeleteIds.forEach { id ->
                if (deleteVaccineUseCase.invoke(id) is ResponseResult.Error) {
                    hasError = true
                }
            }

            if (hasError) {
                _viewState.tryEmit(VaccineRegisterViewState.Error("Não foi possível salvar todas as alterações. Tente novamente."))
            } else {
                _viewState.tryEmit(VaccineRegisterViewState.RegisterSuccess)
            }
        }
    }

}

sealed class VaccineRegisterViewState {
    data object Initial : VaccineRegisterViewState()
    data object Loading : VaccineRegisterViewState()
    data class Error(val msg: String) : VaccineRegisterViewState()
    data object RegisterSuccess : VaccineRegisterViewState()
}
