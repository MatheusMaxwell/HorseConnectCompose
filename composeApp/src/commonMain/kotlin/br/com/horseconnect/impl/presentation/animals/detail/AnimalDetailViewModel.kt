package br.com.horseconnect.impl.presentation.animals.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.usecase.GetAnimalByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnimalDetailViewModel(
    private val getAnimalById: GetAnimalByIdUseCase
): ViewModel() {

    private val _animalState: MutableStateFlow<AnimalDetailViewState> = MutableStateFlow(
        AnimalDetailViewState.Initial
    )
    val animalState: StateFlow<AnimalDetailViewState> get() = _animalState

    var animals: List<Animal> = emptyList()

    fun getAnimalById(animalId: String) {
        _animalState.tryEmit(AnimalDetailViewState.Loading)
        viewModelScope.launch {
            when(val result = getAnimalById.invoke(animalId)) {
                is ResponseResult.Success -> {
                    _animalState.tryEmit(AnimalDetailViewState.Populated(result.data))
                }
                is ResponseResult.Error -> {
                    _animalState.tryEmit(AnimalDetailViewState.Error(result.resultError.msg))
                }
            }
        }
    }
}

sealed class AnimalDetailViewState() {
    data object Initial : AnimalDetailViewState()
    data object Loading : AnimalDetailViewState()
    data class Error(val msg: String) : AnimalDetailViewState()
    data class Populated(val data: Animal) : AnimalDetailViewState()
}