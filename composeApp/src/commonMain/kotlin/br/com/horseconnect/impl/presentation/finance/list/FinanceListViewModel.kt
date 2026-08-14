package br.com.horseconnect.impl.presentation.finance.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.common.viewstate.BaseViewState
import br.com.horseconnect.impl.domain.model.FinanceEntry
import br.com.horseconnect.impl.domain.usecase.finance.GetFinanceEntriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FinanceListViewModel(
    private val getFinanceEntriesUseCase: GetFinanceEntriesUseCase
): ViewModel() {

    private val _state: MutableStateFlow<BaseViewState<List<FinanceEntry>>> = MutableStateFlow(
        BaseViewState.Initial
    )
    val state: StateFlow<BaseViewState<List<FinanceEntry>>> get() = _state

    var entries: List<FinanceEntry> = emptyList()

    fun getFinanceEntries() {
        _state.tryEmit(BaseViewState.Loading)
        viewModelScope.launch {
            when(val result = getFinanceEntriesUseCase.invoke()) {
                is ResponseResult.Success -> {
                    entries = result.data
                    _state.tryEmit(BaseViewState.Populated(entries))
                }
                is ResponseResult.Error -> {
                    _state.tryEmit(BaseViewState.Error(result.resultError.msg))
                }
            }
        }
    }

    fun getFinanceEntriesFiltered(category: String?) {
        viewModelScope.launch {
            _state.tryEmit(
                BaseViewState.Populated(
                    category?.let { value -> entries.filter { it.category == value } } ?: entries
                )
            )
        }
    }

    fun getSearchFinanceEntries(query: String) {
        viewModelScope.launch {
            _state.tryEmit(
                BaseViewState.Populated(
                    entries.filter {
                        it.description.lowercase().contains(query.lowercase()) ||
                                it.category.lowercase().contains(query.lowercase())
                    }
                )
            )
        }
    }
}
