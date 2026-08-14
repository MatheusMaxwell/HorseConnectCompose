package br.com.horseconnect.impl.presentation.pregnancies.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Pregnancy
import br.com.horseconnect.impl.domain.usecase.pregnancy.DeletePregnancyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PregnancyDetailViewModel(
    private val deletePregnancyUseCase: DeletePregnancyUseCase
): ViewModel() {

    private val _viewState: MutableStateFlow<PregnancyDetailViewState> = MutableStateFlow(
        PregnancyDetailViewState.Initial
    )
    val viewState: StateFlow<PregnancyDetailViewState> get() = _viewState

    fun deletePregnancy(pregnancy: Pregnancy) {
        _viewState.tryEmit(PregnancyDetailViewState.Loading)
        viewModelScope.launch {
            when(val result = deletePregnancyUseCase.invoke(pregnancy.id)) {
                is ResponseResult.Success -> {
                    _viewState.tryEmit(PregnancyDetailViewState.DeletedSuccess)
                }
                is ResponseResult.Error -> {
                    _viewState.tryEmit(PregnancyDetailViewState.Error(result.resultError.msg))
                }
            }
        }
    }
}

sealed class PregnancyDetailViewState {
    data object Initial : PregnancyDetailViewState()
    data object Loading : PregnancyDetailViewState()
    data class Error(val msg: String) : PregnancyDetailViewState()
    data object DeletedSuccess : PregnancyDetailViewState()
}
