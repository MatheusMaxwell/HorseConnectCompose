package br.com.horseconnect.impl.presentation.vaccines.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.common.viewstate.BaseViewState
import br.com.horseconnect.impl.domain.model.Vaccine
import br.com.horseconnect.impl.domain.usecase.vaccine.GetVaccinesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VaccineListViewModel(
    private val getVaccinesUseCase: GetVaccinesUseCase
): ViewModel() {

    private val _state: MutableStateFlow<BaseViewState<List<Vaccine>>> = MutableStateFlow(
        BaseViewState.Initial
    )
    val state: StateFlow<BaseViewState<List<Vaccine>>> get() = _state

    var vaccines: List<Vaccine> = emptyList()

    fun getVaccines() {
        _state.tryEmit(BaseViewState.Loading)
        viewModelScope.launch {
            when(val result = getVaccinesUseCase.invoke()) {
                is ResponseResult.Success -> {
                    vaccines = result.data
                    _state.tryEmit(BaseViewState.Populated(vaccines))
                }
                is ResponseResult.Error -> {
                    _state.tryEmit(BaseViewState.Error(result.resultError.msg))
                }
            }
        }
    }

    fun getVaccinesFiltered(name: String?) {
        viewModelScope.launch {
            _state.tryEmit(
                BaseViewState.Populated(
                    name?.let { value -> vaccines.filter { it.name == value } } ?: vaccines
                )
            )
        }
    }

    fun getSearchVaccines(query: String) {
        viewModelScope.launch {
            _state.tryEmit(
                BaseViewState.Populated(
                    vaccines.filter {
                        it.animalName.lowercase().contains(query.lowercase()) ||
                                it.name.lowercase().contains(query.lowercase())
                    }
                )
            )
        }
    }
}
