package br.com.horseconnect.impl.presentation.animals.genealogy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Genealogy
import br.com.horseconnect.impl.domain.usecase.animal.GetGenealogyByAnimalIdUseCase
import br.com.horseconnect.impl.domain.usecase.animal.UpdateGenealogyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GenealogyViewModel(
    private val getGenealogy: GetGenealogyByAnimalIdUseCase,
    private val updateGenealogyUseCase: UpdateGenealogyUseCase
): ViewModel() {
    private val _genealogyState: MutableStateFlow<GenealogyViewState> = MutableStateFlow(
        GenealogyViewState.Initial
    )
    val genealogyState: StateFlow<GenealogyViewState> get() = _genealogyState

    fun getGenealogyByAnimalId(animalId: String) {
        viewModelScope.launch {
            when(val result = getGenealogy.invoke(animalId)) {
                is ResponseResult.Success -> {
                    _genealogyState.tryEmit(GenealogyViewState.Populated(result.data))
                }
                is ResponseResult.Error -> {
                    _genealogyState.tryEmit(GenealogyViewState.Error(result.resultError.msg))
                }
            }
        }
    }

    fun updateGenealogy(genealogy: Genealogy) {
        viewModelScope.launch {
            _genealogyState.tryEmit(GenealogyViewState.Saving)
            when(val result = updateGenealogyUseCase.invoke(genealogy)) {
                is ResponseResult.Success -> {
                    _genealogyState.tryEmit(GenealogyViewState.UpdateSuccess(genealogy))
                }
                is ResponseResult.Error -> {
                    _genealogyState.tryEmit(GenealogyViewState.Error(result.resultError.msg))
                }
            }
        }
    }

}

sealed class GenealogyViewState() {
    data object Initial : GenealogyViewState()
    data object Loading : GenealogyViewState()
    data object Saving : GenealogyViewState()
    data class Error(val msg: String) : GenealogyViewState()
    data class Populated(val data: Genealogy) : GenealogyViewState()
    data class UpdateSuccess(val data: Genealogy) : GenealogyViewState()
}
