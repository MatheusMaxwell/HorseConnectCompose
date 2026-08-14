package br.com.horseconnect.impl.presentation.finance.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.FinanceEntry
import br.com.horseconnect.impl.domain.usecase.finance.DeleteFinanceEntryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FinanceDetailViewModel(
    private val deleteFinanceEntryUseCase: DeleteFinanceEntryUseCase
): ViewModel() {

    private val _viewState: MutableStateFlow<FinanceDetailViewState> = MutableStateFlow(
        FinanceDetailViewState.Initial
    )
    val viewState: StateFlow<FinanceDetailViewState> get() = _viewState

    fun deleteEntry(entry: FinanceEntry) {
        _viewState.tryEmit(FinanceDetailViewState.Loading)
        viewModelScope.launch {
            when(val result = deleteFinanceEntryUseCase.invoke(entry.id)) {
                is ResponseResult.Success -> {
                    _viewState.tryEmit(FinanceDetailViewState.DeletedSuccess)
                }
                is ResponseResult.Error -> {
                    _viewState.tryEmit(FinanceDetailViewState.Error(result.resultError.msg))
                }
            }
        }
    }
}

sealed class FinanceDetailViewState {
    data object Initial : FinanceDetailViewState()
    data object Loading : FinanceDetailViewState()
    data class Error(val msg: String) : FinanceDetailViewState()
    data object DeletedSuccess : FinanceDetailViewState()
}
