package br.com.horseconnect.impl.presentation.vaccines.groupdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.usecase.vaccine.DeleteVaccineUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VaccineGroupDetailViewModel(
    private val deleteVaccineUseCase: DeleteVaccineUseCase
): ViewModel() {

    private val _viewState: MutableStateFlow<VaccineGroupDetailViewState> = MutableStateFlow(
        VaccineGroupDetailViewState.Initial
    )
    val viewState: StateFlow<VaccineGroupDetailViewState> get() = _viewState

    fun deleteGroup(ids: List<String>) {
        _viewState.tryEmit(VaccineGroupDetailViewState.Loading)
        viewModelScope.launch {
            var hasError = false

            ids.forEach { id ->
                if (deleteVaccineUseCase.invoke(id) is ResponseResult.Error) {
                    hasError = true
                }
            }

            if (hasError) {
                _viewState.tryEmit(VaccineGroupDetailViewState.Error("Não foi possível excluir a vacina. Tente novamente."))
            } else {
                _viewState.tryEmit(VaccineGroupDetailViewState.DeletedSuccess)
            }
        }
    }
}

sealed class VaccineGroupDetailViewState {
    data object Initial : VaccineGroupDetailViewState()
    data object Loading : VaccineGroupDetailViewState()
    data class Error(val msg: String) : VaccineGroupDetailViewState()
    data object DeletedSuccess : VaccineGroupDetailViewState()
}
