package br.com.horseconnect.impl.presentation.finance.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.FinanceEntry
import br.com.horseconnect.impl.domain.usecase.finance.RegisterFinanceEntryUseCase
import br.com.horseconnect.impl.domain.usecase.finance.UpdateFinanceEntryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FinanceRegisterViewModel(
    private val registerFinanceEntryUseCase: RegisterFinanceEntryUseCase,
    private val updateFinanceEntryUseCase: UpdateFinanceEntryUseCase
): ViewModel() {

    private val _viewState: MutableStateFlow<FinanceRegisterViewState> = MutableStateFlow(
        FinanceRegisterViewState.Initial
    )
    val viewState: StateFlow<FinanceRegisterViewState> get() = _viewState

    fun register(entry: FinanceEntry) {
        _viewState.tryEmit(FinanceRegisterViewState.Loading)
        viewModelScope.launch {
            when(val result = registerFinanceEntryUseCase.invoke(entry)) {
                is ResponseResult.Success -> {
                    _viewState.tryEmit(FinanceRegisterViewState.RegisterSuccess)
                }
                is ResponseResult.Error -> {
                    _viewState.tryEmit(FinanceRegisterViewState.Error(result.resultError.msg))
                }
            }
        }
    }

    fun update(entry: FinanceEntry) {
        _viewState.tryEmit(FinanceRegisterViewState.Loading)
        viewModelScope.launch {
            when(val result = updateFinanceEntryUseCase.invoke(entry)) {
                is ResponseResult.Success -> {
                    _viewState.tryEmit(FinanceRegisterViewState.RegisterSuccess)
                }
                is ResponseResult.Error -> {
                    _viewState.tryEmit(FinanceRegisterViewState.Error(result.resultError.msg))
                }
            }
        }
    }
}

sealed class FinanceRegisterViewState {
    data object Initial : FinanceRegisterViewState()
    data object Loading : FinanceRegisterViewState()
    data class Error(val msg: String) : FinanceRegisterViewState()
    data object RegisterSuccess : FinanceRegisterViewState()
}
