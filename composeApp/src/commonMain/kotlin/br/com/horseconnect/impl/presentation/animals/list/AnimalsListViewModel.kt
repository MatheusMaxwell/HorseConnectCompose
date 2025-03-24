package br.com.horseconnect.impl.presentation.animals.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.AnimalType
import br.com.horseconnect.impl.domain.usecase.GetAnimalsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnimalsListViewModel(
    private val getAnimalsUseCase: GetAnimalsUseCase
): ViewModel() {

    private val _animalsState: MutableStateFlow<AnimalsViewState> = MutableStateFlow(
        AnimalsViewState.Initial
    )
    val animalsState: StateFlow<AnimalsViewState> get() = _animalsState

    var animals: List<Animal> = emptyList()

    fun getAnimals() {
        _animalsState.tryEmit(AnimalsViewState.Loading)
        viewModelScope.launch {
            when(val result = getAnimalsUseCase.invoke()) {
                is ResponseResult.Success -> {
                    animals = result.data
                    _animalsState.tryEmit(AnimalsViewState.Populated(animals))
                }
                is ResponseResult.Error -> {
                    _animalsState.tryEmit(AnimalsViewState.Error(result.resultError.msg))
                }
            }
        }
    }

    fun getAnimalsFiltered(type: AnimalType?) {
        viewModelScope.launch {
            _animalsState.tryEmit(
                AnimalsViewState.Populated(
                    type?.let { type -> animals.filter { it.types.contains(type) } } ?: animals
                )
            )
        }
    }

    fun getSearchAnimals(query: String) {
        viewModelScope.launch {
            _animalsState.tryEmit(
                AnimalsViewState.Populated(
                    animals.filter { it.name.lowercase().contains(query.lowercase()) }
                )
            )
        }
    }
}

sealed class AnimalsViewState() {
    data object Initial : AnimalsViewState()
    data object Loading : AnimalsViewState()
    data class Error(val msg: String) : AnimalsViewState()
    data class Populated(val data: List<Animal>) : AnimalsViewState()
}