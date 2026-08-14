package br.com.horseconnect.impl.presentation.pregnancies.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.common.viewstate.BaseViewState
import br.com.horseconnect.impl.domain.model.Pregnancy
import br.com.horseconnect.impl.domain.usecase.pregnancy.GetPregnanciesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PregnancyListViewModel(
    private val getPregnanciesUseCase: GetPregnanciesUseCase
): ViewModel() {

    private val _state: MutableStateFlow<BaseViewState<List<Pregnancy>>> = MutableStateFlow(
        BaseViewState.Initial
    )
    val state: StateFlow<BaseViewState<List<Pregnancy>>> get() = _state

    var pregnancies: List<Pregnancy> = emptyList()

    fun getPregnancies() {
        _state.tryEmit(BaseViewState.Loading)
        viewModelScope.launch {
            when(val result = getPregnanciesUseCase.invoke()) {
                is ResponseResult.Success -> {
                    pregnancies = result.data
                    _state.tryEmit(BaseViewState.Populated(pregnancies))
                }
                is ResponseResult.Error -> {
                    _state.tryEmit(BaseViewState.Error(result.resultError.msg))
                }
            }
        }
    }

    fun getPregnanciesFiltered(status: String?) {
        viewModelScope.launch {
            _state.tryEmit(
                BaseViewState.Populated(
                    status?.let { value -> pregnancies.filter { it.status == value } } ?: pregnancies
                )
            )
        }
    }

    fun getSearchPregnancies(query: String) {
        viewModelScope.launch {
            _state.tryEmit(
                BaseViewState.Populated(
                    pregnancies.filter {
                        it.femaleName.lowercase().contains(query.lowercase()) ||
                                it.maleName.lowercase().contains(query.lowercase())
                    }
                )
            )
        }
    }
}
