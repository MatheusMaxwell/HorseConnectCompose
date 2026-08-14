package br.com.horseconnect.impl.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.common.viewstate.BaseViewState
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.FinanceEntry
import br.com.horseconnect.impl.domain.model.Pregnancy
import br.com.horseconnect.impl.domain.model.Vaccine
import br.com.horseconnect.impl.domain.usecase.animal.GetAnimalsUseCase
import br.com.horseconnect.impl.domain.usecase.finance.GetFinanceEntriesUseCase
import br.com.horseconnect.impl.domain.usecase.pregnancy.GetPregnanciesUseCase
import br.com.horseconnect.impl.domain.usecase.vaccine.GetVaccinesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReportsViewModel(
    private val getAnimalsUseCase: GetAnimalsUseCase,
    private val getPregnanciesUseCase: GetPregnanciesUseCase,
    private val getVaccinesUseCase: GetVaccinesUseCase,
    private val getFinanceEntriesUseCase: GetFinanceEntriesUseCase
): ViewModel() {

    private val _state: MutableStateFlow<BaseViewState<ReportsData>> = MutableStateFlow(BaseViewState.Initial)
    val state: StateFlow<BaseViewState<ReportsData>> get() = _state

    fun loadReports() {
        _state.tryEmit(BaseViewState.Loading)
        getAnimals { animals ->
            getPregnancies { pregnancies ->
                getVaccines { vaccines ->
                    getFinanceEntries { financeEntries ->
                        _state.tryEmit(
                            BaseViewState.Populated(
                                ReportsData(
                                    animals = animals,
                                    pregnancies = pregnancies,
                                    vaccines = vaccines,
                                    financeEntries = financeEntries
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getAnimals(onResult: (List<Animal>) -> Unit) {
        viewModelScope.launch {
            when(val result = getAnimalsUseCase.invoke()) {
                is ResponseResult.Success -> onResult.invoke(result.data)
                is ResponseResult.Error -> _state.tryEmit(BaseViewState.Error(result.resultError.msg))
            }
        }
    }

    private fun getPregnancies(onResult: (List<Pregnancy>) -> Unit) {
        viewModelScope.launch {
            when(val result = getPregnanciesUseCase.invoke()) {
                is ResponseResult.Success -> onResult.invoke(result.data)
                is ResponseResult.Error -> _state.tryEmit(BaseViewState.Error(result.resultError.msg))
            }
        }
    }

    private fun getVaccines(onResult: (List<Vaccine>) -> Unit) {
        viewModelScope.launch {
            when(val result = getVaccinesUseCase.invoke()) {
                is ResponseResult.Success -> onResult.invoke(result.data)
                is ResponseResult.Error -> _state.tryEmit(BaseViewState.Error(result.resultError.msg))
            }
        }
    }

    private fun getFinanceEntries(onResult: (List<FinanceEntry>) -> Unit) {
        viewModelScope.launch {
            when(val result = getFinanceEntriesUseCase.invoke()) {
                is ResponseResult.Success -> onResult.invoke(result.data)
                is ResponseResult.Error -> _state.tryEmit(BaseViewState.Error(result.resultError.msg))
            }
        }
    }
}

data class ReportsData(
    val animals: List<Animal>,
    val pregnancies: List<Pregnancy>,
    val vaccines: List<Vaccine>,
    val financeEntries: List<FinanceEntry>
)
